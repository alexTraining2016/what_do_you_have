package comalexpolyanskyi.github.foodandhealth;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import comalexpolyanskyi.github.foodandhealth.utils.AppExecutorService;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;

import static android.content.ContentValues.TAG;

public class App extends Application {

    private static MySimpleImageLoader mySimpleImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        ContextHolder.setContext(this);
        AppHttpClient.install();
        AppExecutorService.install();
    }

    public static MySimpleImageLoader getImageLoader() {
        if (mySimpleImageLoader == null) {
            mySimpleImageLoader = MySimpleImageLoader.Impl.newInstance();
        }
        return mySimpleImageLoader;
    }
}
