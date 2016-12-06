package comalexpolyanskyi.github.foodandhealth.dao;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

public class DAButtonDAO implements InteractionContract.DAO<String> {

    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService executorService;
    private InteractionContract.RequiredPresenter<Void> presenter;
    private Handler handler;
    private AppHttpClient httpClient;

    public DAButtonDAO(@NonNull InteractionContract.RequiredPresenter<Void> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    @Override
    public void get(final String parameters, final boolean forceUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final byte[] requestBytes = httpClient.loadDataFromHttp(parameters, false);
                if (requestBytes != null) {
                    final String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));
                    try {
                        final JSONObject object = new JSONObject(requestString);
                        final boolean isSuccess = object.getBoolean("successfully");
                        if (!isSuccess) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    presenter.onError();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                presenter.onError();
                            }
                        });
                    }
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onError();
                        }
                    });
                }
            }
        });
    }
}
