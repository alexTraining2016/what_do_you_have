package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

abstract class BaseDAO <T,D>  implements MVPContract.DAO<ParametersInformationRequest>  {

    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService executorService;
    private MVPContract.RequiredPresenter<T> presenter;
    private Handler handler;
    private AppHttpClient httpClient;
    private DbOperations operations;

    protected BaseDAO(@NonNull MVPContract.RequiredPresenter<T> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    @Override
    public void get(final ParametersInformationRequest parameters, final boolean notUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isNeedUpdate = false;
                Cursor cursor = getFromCache(parameters.getSelectParameters());
                if(cursor.getCount() != 0) {
                    displayDataFromCache(prepareResponse(cursor));
                    Long currentTime = System.currentTimeMillis();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        Long recordingTime = cursor.getLong(cursor.getColumnIndex(CachedTable.RECORDING_TIME));
                        Long agingTime = cursor.getLong(cursor.getColumnIndex(CachedTable.AGING_TIME));
                        if (agingTime > currentTime - recordingTime) {
                            isNeedUpdate = true;
                            break;
                        }
                    }
                }else{
                    isNeedUpdate = true;
                }
                if(isNeedUpdate && !notUpdate){
                    try {
                        cursor = update(parameters);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        sendAnswer(prepareResponse(cursor));
                    }
                }
            }
        });
    }

    private void displayDataFromCache(final T response){
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenter.onSuccess(response);
            }
        });
    }

    private void sendAnswer(final T request) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(request != null){
                        presenter.onSuccess(request);
                }else{
                    presenter.onError();
                }
            }
        });
    }

    private Cursor update(ParametersInformationRequest parameters){
        byte [] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        if(requestBytes != null){
            String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));
            if(processRequest(requestString)){
                return getFromCache(parameters.getSelectParameters());
            }
        }
        return  null;
    }

    private Cursor getFromCache(String parameters){
        return operations.query(parameters);
    }

    private boolean processRequest(String request){
        try {
            Type type = new TypeToken<List<D>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            final List<D> result = gson.fromJson(request, type);
            List<ContentValues> contentValuesList = prepareContentValues(result);
            saveToCache(contentValuesList);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void saveToCache(List<ContentValues> contentValuesList){
        operations.bulkUpdate(ArticleDescription.class, contentValuesList);
    }

    protected abstract List<ContentValues> prepareContentValues(List<D> result);

    protected abstract T prepareResponse(Cursor cursor);
}
