package comalexpolyanskyi.github.foodandhealth.models;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.support.test.espresso.core.deps.guava.io.ByteStreams;
import android.support.v4.util.SparseArrayCompat;

import java.io.IOException;
import java.io.InputStream;

import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

public class ListFragmentModel implements AppHttpClient.HttpResponse, IMVPContract.Model {

    private IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemBean>> presenter;
    private SparseArrayCompat<ListItemBean> sparseArrays = null;

    public ListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getData(final String url) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                httpClient.loadDataFromHttp(url, ListFragmentModel.this);
                return null;
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        //if i need, here I can clean cache
    }

    @Override
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
        if(sparseArrays != null) {
            presenter.onSuccess(sparseArrays);
        }else{
            presenter.onError();
        }

    }

    @Override
    public void onFail(IOException e) {
        presenter.onError();
        //sparseArrays = DAO.loadCachedData(url);
    }
}
