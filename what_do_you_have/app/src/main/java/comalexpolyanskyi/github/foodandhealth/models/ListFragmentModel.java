package comalexpolyanskyi.github.foodandhealth.models;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import java.io.IOException;

import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

public class ListFragmentModel implements IMVPContract.Model {

    private IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemBean>> presenter;
    private SparseArrayCompat<ListItemBean> sparseArrays = null;

    public ListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getData(final String url) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, SparseArrayCompat<ListItemBean>>() {
            @Override
            protected SparseArrayCompat<ListItemBean> doInBackground(Void... voids) {
                try {
                    String requestHttp = httpClient.loadDataFromHttp(url);
                    //sparseArrays = parseJsonTo(requestHttp);
                } catch (IOException e) {
                    //sparseArrays = DAO.loadCachedData(url);
                }
                return sparseArrays;
            }

            @Override
            protected void onPostExecute(SparseArrayCompat<ListItemBean> response) {
                super.onPostExecute(response);
                if(sparseArrays != null) {
                    presenter.onSuccess(response);
                }else{
                    presenter.onError();
                }
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        //if i need, here I can clean cache
    }

}
