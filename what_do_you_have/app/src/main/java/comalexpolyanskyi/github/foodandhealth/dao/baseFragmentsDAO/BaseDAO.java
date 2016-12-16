package comalexpolyanskyi.github.foodandhealth.dao.baseFragmentsDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public abstract class BaseDAO<T> implements InteractionContract.DAO<ParametersInformationRequest> {

    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService executorService;
    private InteractionContract.RequiredPresenter<T> presenter;
    private Handler handler;
    private AppHttpClient httpClient;
    protected DbOperations operations;

    protected BaseDAO(@NonNull InteractionContract.RequiredPresenter<T> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    @Override
    public void get(final ParametersInformationRequest parameters, final boolean forceUpdate, final boolean noUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isNeedUpdate = true;
                final Cursor cacheData = getFromCache(parameters);

                if (cacheData != null && cacheData.getCount() != 0) {
                    displayDataFromCache(prepareResponse(cacheData));

                    if (!forceUpdate) {
                        isNeedUpdate = isDataOutdated(parameters.getUrl(), cacheData);
                    }
                }

                if (isNeedUpdate && !noUpdate) {
                    final Cursor cursor = update(parameters);

                    if (cursor != null) {
                        sendAnswer(prepareResponse(cursor));
                    } else {
                        sendAnswer(null);
                    }
                }
            }
        });
    }

    private boolean isDataOutdated(String url, Cursor cursor) {
        if (url != null) {
            final Long currentTime = System.currentTimeMillis() / 1000;

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                final Long recordingTime = cursor.getLong(cursor.getColumnIndex(CachedTable.RECORDING_TIME));
                final Long agingTime = cursor.getLong(cursor.getColumnIndex(CachedTable.AGING_TIME));

                if (recordingTime <= (currentTime - agingTime)) {

                    return true;
                }
            }
        }

        return false;
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
        final byte[] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        if (requestBytes != null) {
            final String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));
            final List<ContentValues> contentValuesList = processRequest(requestString);
            if (contentValuesList != null) {
                saveToCache(contentValuesList);
                return getFromCache(parameters);
            }
        }

        return null;
    }

    private Cursor getFromCache(ParametersInformationRequest parameters) {
        return operations.query(parameters.getSelectParameters());
    }

    protected abstract void saveToCache(List<ContentValues> contentValuesList);

    protected abstract List<ContentValues> processRequest(@NonNull String request);

    protected abstract T prepareResponse(@NonNull Cursor cursor);
}
