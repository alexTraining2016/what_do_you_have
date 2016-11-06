package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.base.Charsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentDAO implements MVPContract.DAO<ParametersInformationRequest> {

    private MVPContract.RequiredPresenter<Cursor> presenter;
    private Handler handler;
    private final DbOperations operations;
    private ExecutorService executorService;
    private AppHttpClient httpClient;

    public ArticleListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
    }

    private void saveToCache(List<ArticleListItemDO> requestList) {
        List<ContentValues> contentValuesList = new ArrayList<>(requestList.size());
        for (ArticleListItemDO item : requestList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Article.ID, item.getId());
            contentValues.put(Article.TYPE, item.getType());
            contentValues.put(Article.NAME, item.getName());
            contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
            contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Article.AGING_TIME, 3600);
            contentValuesList.add(contentValues);
        }
        operations.bulkUpdate(Article.class, contentValuesList);
    }

    @Override
    public void get(final ParametersInformationRequest parameters){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isNeedUpdate = false;
                Cursor cursor = getFromCache(parameters.getSelectParameters());
                if(cursor.getCount() != 0) {
                    displayDataFromCache(cursor);
                    Long currentTime = System.currentTimeMillis();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        Long recordingTime = cursor.getLong(cursor.getColumnIndex(Article.RECORDING_TIME));
                        Long agingTime = cursor.getLong(cursor.getColumnIndex(Article.AGING_TIME));
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

    private void sendAnswer(final Cursor cursor) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(cursor != null){
                    if(cursor.getCount() > 0){
                        presenter.onSuccess(cursor);
                    }else{
                        presenter.onError();
                    }
                }else{
                    presenter.onError();
                }
            }
        });
    }

    private void displayDataFromCache(final Cursor cursor){
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenter.onSuccess(cursor);
            }
        });
    }

    private Cursor update(ParametersInformationRequest parameters){
        byte [] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        if(requestBytes != null) {
            String requestString = new String(requestBytes, Charsets.UTF_8);
            Type listType = new TypeToken<List<ArticleListItemDO>>() {}.getType();
            Gson gson = new GsonBuilder().create();
            final List<ArticleListItemDO> requestList = gson.fromJson(requestString, listType);
            saveToCache(requestList);
            return getFromCache(parameters.getSelectParameters());
        }else{
            return null;
        }
    }

    private Cursor getFromCache(String[] parameters){
        return operations.query(parameters[0] + DBHelper.getTableName(Article.class) + parameters[1]);
    }
}
