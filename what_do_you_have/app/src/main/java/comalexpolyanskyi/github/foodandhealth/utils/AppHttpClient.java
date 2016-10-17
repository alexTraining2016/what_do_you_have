package comalexpolyanskyi.github.foodandhealth.utils;

import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.ByteStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppHttpClient {

    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String MAX_STALE = "max-stale=";
    private static AppHttpClient httpClient;
    private final int maxStale = 60 * 60;

    private AppHttpClient(){}

    public String loadDataFromHttp(String stringUrl) throws  IOException {
        String result = null;
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(true);
            urlConnection.addRequestProperty(CACHE_CONTROL, MAX_STALE + maxStale);
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            result = new String(ByteStreams.toByteArray(inputStream), Charsets.UTF_8);
        return result;
    }

    public static void install(){
        if(httpClient == null){
            httpClient = new AppHttpClient();
        }
    }

    public static AppHttpClient getAppHttpClient() throws NullPointerException {
        if(httpClient != null){
            return httpClient;
        }else{
            throw new NullPointerException();
        }
    }
}
