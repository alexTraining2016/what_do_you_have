package comalexpolyanskyi.github.foodandhealth.dao;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

/**
 * Created by Алексей on 07.11.2016.
 */

abstract class AbstractDAO<T>  implements MVPContract.DAO<ParametersInformationRequest>  {

    private ExecutorService executorService;
    protected MVPContract.RequiredPresenter<T> presenter;
    protected Handler handler;
    protected final DbOperations operations;
    protected AppHttpClient httpClient;

    protected AbstractDAO(@NonNull MVPContract.RequiredPresenter<T> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    @Override
    public void get(final ParametersInformationRequest parameters) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isNeedUpdate = false;
                Cursor cursor = getFromCache(parameters.getSelectParameters());
                if(cursor.getCount() != 0) {
                    displayDataFromCache(cursor);
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
                if(isNeedUpdate){
                    cursor = update(parameters);
                }
                sendAnswer(cursor);
            }
        });
    }

    protected void sendAnswer(final Cursor cursor) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(cursor != null){
                    if(cursor.getCount() > 0){
                        presenter.onSuccess((T) cursor);
                    }else{
                        presenter.onError();
                    }
                }else{
                    presenter.onError();
                }
            }
        });
    }

    protected void displayDataFromCache(final Cursor cursor){
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenter.onSuccess((T) cursor);
            }
        });
    }

    protected abstract Cursor update(ParametersInformationRequest parameters);

    protected abstract Cursor getFromCache(String[] parameters);
}
