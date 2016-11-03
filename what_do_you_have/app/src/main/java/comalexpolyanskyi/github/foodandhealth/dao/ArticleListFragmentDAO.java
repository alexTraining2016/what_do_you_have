package comalexpolyanskyi.github.foodandhealth.dao;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.base.Charsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentDAO implements IMVPContract.DAO<ParametersInformationRequest> {

    static final String FOOD_AND_HEAL = "foodAndHeal";
    static final int VERSION = 1;
    private IMVPContract.RequiredPresenter<List<ArticleListItemDO>> presenter;
    private Handler handler;

    public ArticleListFragmentDAO(@NonNull IMVPContract.RequiredPresenter<List<ArticleListItemDO>> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
    }

    @Override
    public void get(final ParametersInformationRequest parameters) {
        final AppHttpClient httpClient = AppHttpClient.getAppHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                byte [] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl());
                if(requestBytes != null) {
                    String requestString = new String(requestBytes, Charsets.UTF_8);
                    Type listType = new TypeToken<List<ArticleListItemDO>>(){}.getType();
                    Gson gson =  new GsonBuilder().create();
                    final List<ArticleListItemDO> requestList = gson.fromJson(requestString, listType);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onSuccess(requestList);
                        }
                    });
                }else{
                    final List<ArticleListItemDO> requestList = getDataFromCache(parameters.getSelectParameters());
                    if(requestList.size() != 0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                presenter.onSuccess(requestList);
                            }
                        });
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onError();
                        }
                    });
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void delete(ParametersInformationRequest parameters) {

    }

    @Override
    public void post(ParametersInformationRequest parameters) {
    }

    @Override
    public void put(ParametersInformationRequest parameters) {

    }

    @NonNull
    private List<ArticleListItemDO> getDataFromCache(HashMap <String, String> parameters){
        final DbOperations operations = new DBHelper(ContextHolder.getContext(), FOOD_AND_HEAL, VERSION);
        final Cursor cursor = operations.query("SELECT * FROM " + DBHelper.getTableName(Article.class) +  parameters.get(Article.TYPE));
        List<ArticleListItemDO> data = new ArrayList<>(0);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ArticleListItemDO articleListItemDO = new ArticleListItemDO(cursor.getInt(cursor.getColumnIndex(Article.ID)),
                                                                        cursor.getString(cursor.getColumnIndex(Article.NAME)),
                                                                        cursor.getString(cursor.getColumnIndex(Article.IMAGE_URI)));
            data.add(articleListItemDO);
        }
        cursor.close();
        return data;
    }
}
