package comalexpolyanskyi.github.foodandhealth;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by Алексей on 13.10.2016.
 */

public class App extends Application {

    public static final String HTTP = "http";
    public static final String CACHE_FAILED = "HTTP response cache installation failed:";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            File httpCacheDir = new File(this.getCacheDir(), HTTP);
            long httpCacheSize = 5 * 1024 * 1024;
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG, CACHE_FAILED + e);
        }
    }
}
