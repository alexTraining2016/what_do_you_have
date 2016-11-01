package comalexpolyanskyi.github.foodandhealth.models;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.models.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

import static android.support.test.espresso.core.deps.guava.base.Charsets.UTF_8;

public class ArticleListFragmentModel implements IMVPContract.Model {

    private IMVPContract.RequiredPresenter<List<ArticleListItemDO>> presenter;
    private Handler handler;

    public ArticleListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
    }

    @Override
    public void getData(final String url) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                byte [] requestBytes = httpClient.loadDataFromHttp(url);
//              ArticleListItemDO[] articleItemArray = gson.fromJson(requestString, ArticleListItemDO[].class);
                //if(articleItemArray != null) {
                if(requestBytes != null) {
                    String requestString = new String(requestBytes, UTF_8);
                    //String requestString = Arrays.toString(requestBytes);
                    Log.i("123", url + "  " + requestString);
                    Type listType = new TypeToken<List<ArticleListItemDO>>(){}.getType();
                    Gson gson =  new GsonBuilder().create();
                    final List<ArticleListItemDO> requestList = gson.fromJson(requestString, listType);
                   // final List<ArticleListItemDO> requestList = new ArrayList<>(Arrays.asList(articleItemArray));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onSuccess(requestList);
                        }
                    });
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onError();
                        }
                    });
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        //if i need, here I can clean cache
    }

   /* @Override
    public void onSuccess(InputStream inputStream) {
        String httpRequest = null;
        try {
            httpRequest = new String(ByteStreams.toByteArray(inputStream), Charsets.UTF_8);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            //sparseArrays = DAO.loadCachedData(url);
        }
        //sparseArrays = parseJsonTo(requestHttp);

    }

    @Override
    public void onFail(IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenter.onError();
            }
        });
        //sparseArrays = DAO.loadCachedData(url);
    }*/
}
