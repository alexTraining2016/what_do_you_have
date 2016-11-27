package comalexpolyanskyi.github.foodandhealth;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;

import static android.content.ContentValues.TAG;

public class App extends Application {

    public static final String HTTP = "http";
    public static final String CACHE_FAILED = "HTTP response cache installation failed:";

    private static MySimpleImageLoader mySimpleImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.setContext(this);
        AppHttpClient.install();

        try {
            final File httpCacheDir = new File(this.getCacheDir(), HTTP);
            final long httpCacheSize = 5 * 1024 * 1024;
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG, CACHE_FAILED + e);
        }
    }


    public static MySimpleImageLoader getImageLoader() {
        if (mySimpleImageLoader == null) {
            mySimpleImageLoader = MySimpleImageLoader.Impl.newInstance();
        }
        return mySimpleImageLoader;
    }
}
