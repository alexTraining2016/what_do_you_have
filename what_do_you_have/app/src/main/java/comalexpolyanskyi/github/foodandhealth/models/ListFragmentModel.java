package comalexpolyanskyi.github.foodandhealth.models;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.io.IOException;
import java.io.Serializable;

import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

/**
 * Created by Алексей on 17.10.2016.
 */

public class ListFragmentModel<T extends Serializable> implements IMVPContract.Model {

    private IMVPContract.RequiredPresenter<T> presenter;
    private SparseArrayCompat<T> sparseArrays = null;

    public ListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getListItems(final String url) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, SparseArrayCompat<T>>() {
            @Override
            protected SparseArrayCompat<T> doInBackground(Void... voids) {
                try {
                    String requestHttp = httpClient.loadDataFromHttp(url);
                    //sparseArrays = parseJsonTo(requestHttp);
                } catch (IOException e) {
                    //sparseArrays = DAO.loadCachedData(url);
                }
                return sparseArrays;
            }

            @Override
            protected void onPostExecute(SparseArrayCompat<T> response) {
                super.onPostExecute(response);
                if(sparseArrays != null) {
                    presenter.onSuccess(response);
                }else{
                    presenter.onError();
                }
            }
        }.execute();
    }

}
