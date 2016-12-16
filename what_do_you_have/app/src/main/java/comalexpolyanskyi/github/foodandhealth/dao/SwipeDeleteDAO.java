package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.BindingUserDataDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.FavArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;


public class SwipeDeleteDAO implements InteractionContract.DAO<ParametersInformationRequest> {
    private static final String CHARSET_NAME = "UTF-8";
    private ExecutorService executorService;
    private InteractionContract.RequiredPresenter<Cursor> presenter;
    private Handler handler;
    private AppHttpClient httpClient;
    private DbOperations operations;

    public SwipeDeleteDAO(@NonNull InteractionContract.RequiredPresenter<Cursor> presenter)  {
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
                final Cursor cursor = update(parameters);
                handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(cursor != null) {
                                presenter.onSuccess(cursor);
                            }else{
                                presenter.onError();
                            }
                        }
                    });

            }
        });
    }

    @Nullable
    private Cursor update(ParametersInformationRequest parameters) {
        final byte[] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), false);
        if (requestBytes != null) {
            final String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));
            final List<ContentValues> contentValuesList = processRequest(requestString);
            if (contentValuesList != null) {
                operations.bulkInsert(Article.class, contentValuesList);
                return operations.query(parameters.getSelectParameters());
            }
        }

        return null;
    }

    private List<ContentValues> processRequest(@NonNull String request) {
        final List<ContentValues> contentValuesList = new ArrayList<>();

        try {
            final Type type = new TypeToken<List<FavArticleListItemDO>>(){
            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<FavArticleListItemDO> result = gson.fromJson(request, type);
            for (FavArticleListItemDO item : result) {
                final ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
                processingAdditionalData(item.getUserDataList());
            }
        } catch (Exception e) {

            return null;
        }

        return contentValuesList;
    }

    private ContentValues prepareContentValues(FavArticleListItemDO item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Article.ID, item.getId());
        contentValues.put(Article.TYPE, item.getType());
        contentValues.put(Article.NAME, item.getName());
        contentValues.put(Article.SEARCH_NAME, item.getName().toLowerCase());
        contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
        contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis()/1000);
        contentValues.put(Article.AGING_TIME, 3600);

        return contentValues;
    }

    private void processingAdditionalData(List<BindingUserDataDO> items){
        final List<ContentValues> list = new ArrayList<>();
        for (BindingUserDataDO item:items) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(Favorites.ID, item.getId());
            contentValues.put(Favorites.ART_ID, item.getArtId());
            contentValues.put(Favorites.USER_ID, item.getUserId());
            contentValues.put(Favorites.ISLIKE, item.getIsLike());
            contentValues.put(Favorites.ISFAVORITES, item.getIsRepost());

            list.add(contentValues);
        }

        operations.bulkInsert(Favorites.class, list);
    }
}
