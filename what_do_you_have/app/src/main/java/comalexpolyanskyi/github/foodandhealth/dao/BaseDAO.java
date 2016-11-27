package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

abstract class BaseDAO<T> implements MVPContract.DAO<ParametersInformationRequest> {

    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService executorService;
    private MVPContract.RequiredPresenter<T> presenter;
    private Handler handler;
    private AppHttpClient httpClient;
    protected DbOperations operations;

    protected BaseDAO(@NonNull MVPContract.RequiredPresenter<T> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    @Override
    public void get(final ParametersInformationRequest parameters, final boolean notUpdate, final boolean forceUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isNeedUpdate = false;
                Cursor cursor = getFromCache(parameters.getSelectParameters());

                if (cursor.getCount() != 0) {
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
                } else {
                    isNeedUpdate = true;
                }
                if ((isNeedUpdate && !notUpdate) || forceUpdate) {
                    cursor = update(parameters);
                    if (cursor != null) {
                        sendAnswer(prepareResponse(cursor));
                    } else {
                        sendAnswer(null);
                    }
                }
            }
        });
    }

    private void displayDataFromCache(final T response) {
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
                if (request != null) {
                    presenter.onSuccess(request);
                } else {
                    presenter.onError();
                }
            }
        });
    }

    @Nullable
    private Cursor update(ParametersInformationRequest parameters) {
        byte[] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        Log.i("123", parameters.getUrl());
        if (requestBytes != null) {
            String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));
            Log.i("123", requestString);
            List<ContentValues> contentValuesList = processRequest(requestString);
            if (contentValuesList != null) {
                saveToCache(contentValuesList);
                return getFromCache(parameters.getSelectParameters());
            }
        }

        return null;
    }

    private Cursor getFromCache(String parameters) {
        return operations.query(parameters);
    }

    protected abstract void saveToCache(List<ContentValues> contentValuesList);

    protected abstract List<ContentValues> processRequest(@NonNull String request);

    protected abstract T prepareResponse(@NonNull Cursor cursor);
}
