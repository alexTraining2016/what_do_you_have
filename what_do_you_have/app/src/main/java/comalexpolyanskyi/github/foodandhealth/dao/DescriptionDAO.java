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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;


public class DescriptionDAO implements MVPContract.DAO<ParametersInformationRequest> {

    private MVPContract.RequiredPresenter<ArticleDO> presenter;
    private Handler handler;
    private final DbOperations operations;
    private ExecutorService executorService;
    private AppHttpClient httpClient;

    public DescriptionDAO(@NonNull MVPContract.RequiredPresenter<ArticleDO> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
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
                        Long recordingTime = cursor.getLong(cursor.getColumnIndex(ArticleDescription.RECORDING_TIME));
                        Long agingTime = cursor.getLong(cursor.getColumnIndex(ArticleDescription.AGING_TIME));
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

    private Cursor update(ParametersInformationRequest parameters){
        byte [] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        if(requestBytes != null) {
            String requestString = new String(requestBytes, Charsets.UTF_8);
            Type type = new TypeToken<List<ArticleDO>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            final List<ArticleDO> result = gson.fromJson(requestString, type);
            saveToCache(result.get(0));
            return getFromCache(parameters.getSelectParameters());
        }else{
            return null;
        }
    }

    private void saveToCache(ArticleDO result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleDescription.ID, result.getId());
        contentValues.put(ArticleDescription.NAME, result.getName());
        contentValues.put(ArticleDescription.DESCRIPTION, result.getDescription());
        contentValues.put(ArticleDescription.LIKE_COUNT, result.getLikeCount());
        contentValues.put(ArticleDescription.REPOST_COUNT, result.getFavCount());
        contentValues.put(ArticleDescription.IMAGE_URI, result.getPhotoUrl());
        contentValues.put(ArticleDescription.RECORDING_TIME, System.currentTimeMillis());
        contentValues.put(ArticleDescription.AGING_TIME, 3600);
        operations.update(ArticleDescription.class, contentValues);
    }


    private Cursor getFromCache(String[] parameters){
        return operations.query(parameters[0] + DBHelper.getTableName(ArticleDescription.class) + parameters[1]);
    }

    private ArticleDO convertToArticleDO(Cursor cursor){
        cursor.moveToFirst();
        ArticleDO request = null;
        try {
            request = new ArticleDO(cursor.getInt(cursor.getColumnIndex(ArticleDescription.ID)),
                    cursor.getString(cursor.getColumnIndex(ArticleDescription.NAME)),
                    cursor.getString(cursor.getColumnIndex(ArticleDescription.DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(ArticleDescription.IMAGE_URI)),
                    cursor.getInt(cursor.getColumnIndex(ArticleDescription.LIKE_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(ArticleDescription.REPOST_COUNT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
    private void sendAnswer(final Cursor cursor) {
        final ArticleDO response = convertToArticleDO(cursor);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(response != null){
                        presenter.onSuccess(response);
                }else{
                    presenter.onError();
                }
            }
        });
    }

    private void displayDataFromCache(final Cursor cursor){
        final ArticleDO response = convertToArticleDO(cursor);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (response != null) {
                    presenter.onSuccess(response);
                }
            }
        });
    }
}
