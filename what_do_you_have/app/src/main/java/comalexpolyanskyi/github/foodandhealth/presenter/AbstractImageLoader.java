package comalexpolyanskyi.github.foodandhealth.presenter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.utils.cache.FileCache;


public abstract class AbstractImageLoader {

    private static final int THREE_POOL_SIZE = 7;
    public static final String TEST = "test";
    public static final String ERROR = "error";
    private Handler handler;
    private ExecutorService executorService;
    protected FileCache fileCache;
    protected LruCache<String, Bitmap>  memoryCache;

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;

    public AbstractImageLoader(){
        fileCache = FileCache.getFileCache();
        memoryCache = new LruCache<>(cacheSize);
        executorService = Executors.newFixedThreadPool(THREE_POOL_SIZE);
    }

    abstract public void onDestroy();

    protected void onPreExecute(){

    }

    abstract protected Bitmap doInBackground(String url);

    abstract protected void onPostExecute(Bitmap response, ImageView imageView);

    public void loadImageFromUrl(String url, final ImageView imageView){
        handler = new Handler(Looper.getMainLooper());
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap == null){
            executorService.submit(new BitmapLoader(url, imageView));
        }else{
            onPostExecute(bitmap, imageView);
        }
    }

    protected Bitmap decodeFile(File f){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            Log.e(TEST, ERROR + e);
        }
        return null;
    }

    private class BitmapLoader implements Runnable {
        String stringUrl;
        ImageView imageView;
        BitmapLoader(String stringUrl, ImageView imageView){
            this.imageView = imageView;
            this.stringUrl=stringUrl;
        }

        @Override
        public void run() {
            File f=fileCache.getFile(stringUrl);
            Bitmap bitmap = decodeFile(f);
            if(bitmap == null) {
                bitmap = doInBackground(stringUrl);
            }
            final Bitmap responseBitmap = bitmap;
            memoryCache.put(stringUrl, responseBitmap);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onPostExecute(responseBitmap, imageView);
                }
            });
        }
    }
}
