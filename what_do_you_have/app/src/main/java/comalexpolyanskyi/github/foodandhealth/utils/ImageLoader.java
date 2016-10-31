package comalexpolyanskyi.github.foodandhealth.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import comalexpolyanskyi.github.foodandhealth.utils.cache.FileCache;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


class ImageLoader implements AntiMalevich {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int FILE_CACHE_COUNT = 40;
    private Handler handler;
    private FileCache fileCache;
    private ExecutorService executorService;
    private LruCache<String, Bitmap>  memoryCache;

    ImageLoader(){
        handler = new Handler(Looper.getMainLooper());
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        fileCache = FileCache.initialFileCache(ContextHolder.getContext(), FILE_CACHE_COUNT);
        //if override the method sizeOf then the memory cache doesn't work ¯\_(ツ)_/¯
        memoryCache = new LruCache<>(cacheSize);
        /*{
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() + key.length();
            }
        };*/
        //executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        executorService = new ThreadPoolExecutor(0, THREAD_POOL_SIZE, 0, MILLISECONDS, new BlockingLifoQueue<Runnable>());
    }

    public void loadImageFromUrl(final String url, final ImageView imageView){
        imageView.setTag(url);
        imageView.setImageBitmap(null);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap == null){
            executorService.submit(new BitmapLoader(url, imageView));
        }else {
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

    private class BitmapLoader implements Runnable {
        private String stringUrl;
        private WeakReference<ImageView> imageViewReference;

        BitmapLoader(String stringUrl, ImageView imageView){
            this.imageViewReference =  new WeakReference<>(imageView);
            this.stringUrl=stringUrl;
        }

        private Bitmap decodeAndResizeFile(byte [] bytes, ImageView imageView){
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o);
            int requiredWidth = imageView.getMeasuredWidth();
            int requiredHeight = imageView.getMeasuredHeight();
            int outWidth = o.outWidth, outHeight = o.outHeight;
            int scale = 1;
             while(true){
                if(outWidth/2 < requiredWidth || outHeight/2 < requiredHeight) {
                    break;
                }
                outWidth /= 2 ;
                outHeight /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = false;
            o2.inSampleSize=scale;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o2);
        }

        @Override
        public void run() {
            Bitmap bitmap = fileCache.get(stringUrl);
            if(bitmap == null) {
                byte[] bytes = AppHttpClient.getAppHttpClient().loadDataFromHttp(stringUrl);
                bitmap = decodeAndResizeFile(bytes, imageViewReference.get());
                fileCache.put(stringUrl, bitmap);
            }
            memoryCache.put(stringUrl, bitmap);
            setImageInView(bitmap, imageViewReference.get(), stringUrl);
        }
    }
}