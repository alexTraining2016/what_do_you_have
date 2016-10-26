package comalexpolyanskyi.github.foodandhealth.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

import comalexpolyanskyi.github.foodandhealth.utils.cache.FileCache;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


class ImageLoader implements AntiMalevich {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_SIZE = CPU_COUNT * 2 + 1;
    private Handler handler;
    private ExecutorService executorService;
    private FileCache fileCache;
    private LruCache<String, Bitmap>  memoryCache;

    ImageLoader(){
        handler = new Handler(Looper.getMainLooper());
        fileCache = FileCache.getFileCache();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        //if override the method sizeOf then the memory cache doesn't work ¯\_(ツ)_/¯
        memoryCache = new LruCache<>(cacheSize);
        /*{
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() + key.length();
            }
        };*/
        //executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        executorService = new ThreadPoolExecutor(0, THREAD_POOL_SIZE, 0, MILLISECONDS, new LinkedBlockingDeque<Runnable>(THREAD_POOL_SIZE));
    }

    public void loadImageFromUrl(final String url, final ImageView imageView){
        imageView.setImageBitmap(null);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap == null){
            executorService.submit(new BitmapLoader(url, imageView));
        }else {
            Log.i("123", "1");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void setImageInView(final Bitmap bitmap, final ImageView imageView, final String stringUrl){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (imageView != null) {
                    Object tag = imageView.getTag();
                    if (tag != null && tag.equals(stringUrl)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }

    private class BitmapLoader implements Runnable, AppHttpClient.HttpResponse {
        private String stringUrl;
        private WeakReference<ImageView> imageViewReference;

        BitmapLoader(String stringUrl, ImageView imageView){
            this.imageViewReference =  new WeakReference<>(imageView);
            this.stringUrl=stringUrl;
            imageView.setTag(stringUrl);
        }

        private Bitmap decodeFile(File f, FileInputStream inputStream){
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                if(f == null) {
                    BitmapFactory.decodeStream(inputStream, null, o);
                }else{
                    BitmapFactory.decodeStream(new FileInputStream(f), null, o);
                }
                final int REQUIRED_SIZE = 150;
                int outWidth = o.outWidth, outHeight = o.outHeight;
                int scale = 1;
                while(true){
                    if(outWidth/2 < REQUIRED_SIZE || outHeight/2 < REQUIRED_SIZE)
                        break;
                    outWidth /= 2 ;
                    outHeight /= 2;
                    scale *= 2;
                }
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                if(f == null){
                    inputStream.close();
                    return BitmapFactory.decodeStream(inputStream, null, o2);
                }else{
                    return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void run() {
            File f=fileCache.getFile(stringUrl);
            Bitmap bitmap = decodeFile(f, null);
            if(bitmap == null) {
                Log.i("123", "3");
                AppHttpClient.getAppHttpClient().loadDataFromHttp(stringUrl, this);
            }else{
                Log.i("123", "2");
                memoryCache.put(stringUrl, bitmap);
                setImageInView(bitmap, imageViewReference.get(), stringUrl);
            }
        }

        @Override
        public void onSuccess(InputStream inputStream) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            try {
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            memoryCache.put(stringUrl, bitmap);
            fileCache.put(stringUrl, bitmap);
            setImageInView(bitmap, imageViewReference.get(), stringUrl);
        }

        @Override
        public void onFail(IOException e) {
            //set default bitmap
        }
    }
}