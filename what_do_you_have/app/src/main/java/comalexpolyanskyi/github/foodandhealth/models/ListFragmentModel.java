package comalexpolyanskyi.github.foodandhealth.models;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

public class ListFragmentModel implements IMVPContract.Model {

    private IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemBean>> presenter;
    private SparseArrayCompat<ListItemBean> sparseArrays = null;
    private Handler handler;

    public ListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
    }

    @Override
    public void getData(final String url) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                byte[] bytes = httpClient.loadDataFromHttp(url);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(sparseArrays != null) {

                            presenter.onSuccess(sparseArrays);
                        }else{
                            presenter.onError();
                        }
                    }
                });
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
