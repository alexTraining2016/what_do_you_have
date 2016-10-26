package comalexpolyanskyi.github.foodandhealth.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppHttpClient {

    private final String CACHE_CONTROL = "Cache-Control";
    private final String MAX_STALE = "max-stale=";
    private static AppHttpClient httpClient;
    private HttpResponse httpResponse;

    public interface HttpResponse{
        void onSuccess(InputStream inputStream);
        void onFail(IOException e);
    }
    private AppHttpClient(){}

    public void loadDataFromHttp(String stringUrl, HttpResponse httpResponse)  {
        this.httpResponse = httpResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(true);
            int maxStale = 60 * 60;
            urlConnection.addRequestProperty(CACHE_CONTROL, MAX_STALE + maxStale);
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            httpResponse.onSuccess(inputStream);
        }catch (IOException e){
            httpResponse.onFail(e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
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
