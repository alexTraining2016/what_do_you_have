package comalexpolyanskyi.github.foodandhealth.utils.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.BlockingLifoQueue;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.cache.FileCache;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class ImageLoader implements MySimpleImageLoader {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 0;
    private static final long FILE_CACHE_SIZE = 300 * 1024 * 1024;
    private Handler handler;
    private FileCache fileCache;
    private ExecutorService executorService;
    private LruCache<String, Bitmap> memoryCache;

    ImageLoader() {
        handler = new Handler(Looper.getMainLooper());
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        fileCache = FileCache.initialFileCache(ContextHolder.getContext(), FILE_CACHE_SIZE);
        memoryCache = new LruCache<>(cacheSize);
        /*{
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() + key.length();
            }
        };*/
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, MILLISECONDS, new BlockingLifoQueue<Runnable>());
    }

    public void loadImageFromUrl(final String url, final ImageView imageView, final Palette.PaletteAsyncListener paletteListener) {
        imageView.setTag(url);
        imageView.setImageBitmap(null);
        final Bitmap bitmap = memoryCache.get(url);

        if (bitmap == null) {
            executorService.submit(new BitmapLoader(url, imageView, paletteListener));
        } else if (paletteListener != null) {
            imageView.setImageBitmap(bitmap);
            Palette.from(bitmap).generate(paletteListener);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void setImageInView(final Bitmap bitmap, final ImageView imageView, final String stringUrl, final Palette.PaletteAsyncListener paletteListener) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (imageView != null) {
                    Object tag = imageView.getTag();

                    if (tag != null && tag.equals(stringUrl)) {
                        imageView.setImageBitmap(bitmap);

                        if (paletteListener != null) {
                            Palette.from(bitmap).generate(paletteListener);
                        }
                    }
                }
            }
        });
    }

    private class BitmapLoader implements Runnable {

        private String stringUrl;
        private WeakReference<ImageView> imageViewReference;
        private Palette.PaletteAsyncListener paletteListener;

        BitmapLoader(final String stringUrl, final ImageView imageView, final Palette.PaletteAsyncListener paletteListener) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.stringUrl = stringUrl;
            this.paletteListener = paletteListener;
        }

        private Bitmap decodeAndResizeFile(byte[] bytes, final ImageView imageView) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o);
            final int requiredWidth = imageView.getMeasuredWidth();
            final int requiredHeight = imageView.getMeasuredHeight();
            int outWidth = o.outWidth, outHeight = o.outHeight;
            int scale = 1;

            while (true) {
                if (outWidth / 2 < requiredWidth || outHeight / 2 < requiredHeight) {
                    break;
                }
                outWidth /= 2;
                outHeight /= 2;
                scale *= 2;
            }

            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inJustDecodeBounds = false;
            o2.inSampleSize = scale;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, o2);
        }

        @Override
        public void run() {
            Bitmap bitmap = fileCache.get(stringUrl);

            if (bitmap == null) {
                final byte[] bytes = AppHttpClient.getAppHttpClient().loadDataFromHttp(stringUrl);
                bitmap = decodeAndResizeFile(bytes, imageViewReference.get());
                fileCache.put(stringUrl, bitmap);
            }

            memoryCache.put(stringUrl, bitmap);
            setImageInView(bitmap, imageViewReference.get(), stringUrl, paletteListener);
        }
    }
}