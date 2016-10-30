package comalexpolyanskyi.github.foodandhealth.utils;

import android.support.test.espresso.core.deps.guava.io.ByteStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppHttpClient {

    private final String CACHE_CONTROL = "Cache-Control";
    private final String MAX_STALE = "max-stale=";
    private static AppHttpClient httpClient;


    private AppHttpClient(){}

    public byte[] loadDataFromHttp(String stringUrl)  {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(true);
            int maxStale = 60 * 60;
            urlConnection.addRequestProperty(CACHE_CONTROL, MAX_STALE + maxStale);
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            bytes = ByteStreams.toByteArray(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return bytes;
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
