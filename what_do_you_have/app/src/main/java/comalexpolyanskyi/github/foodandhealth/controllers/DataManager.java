package comalexpolyanskyi.github.foodandhealth.controllers;

import android.os.AsyncTask;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.ByteStreams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataManager extends AsyncTask<Void, Void, String>  {

    private DataManagerCallback callback;
    private String stringUrl = null;

    interface DataManagerCallback{
        void onDataManagerError(String message);
        void onDataManagerSuccess(String result);
    }

    public DataManager(String stringUrl, DataManagerCallback callback){
        this.callback = callback;
        this.stringUrl = stringUrl;
    }

    @Override
    protected String doInBackground(Void... voids)  {
        final int maxStale = 60 * 60;
        String result = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(true);
            urlConnection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            result = new String(ByteStreams.toByteArray(inputStream), Charsets.UTF_8);
        } catch (IOException e) {
            result = null;
            callback.onDataManagerError(e.toString());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null){
            callback.onDataManagerSuccess(result);
        }
    }
}
