package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.base.Charsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class ArticleListFragmentDAO extends BaseDAO<Cursor> implements MVPContract.DAO<ParametersInformationRequest> {

    public ArticleListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    private void saveToCache(List<ArticleListItemDO> requestList) {
        List<ContentValues> contentValuesList = new ArrayList<>(requestList.size());
        for (ArticleListItemDO item : requestList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Article.ID, item.getId());
            contentValues.put(Article.TYPE, item.getType());
            //костылище что б поиск работал
            contentValues.put(Article.NAME, item.getName().toLowerCase());
            contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
            contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Article.AGING_TIME, 3600);
            contentValuesList.add(contentValues);
        }
        operations.bulkUpdate(Article.class, contentValuesList);
    }

    @Override
    protected Cursor update(ParametersInformationRequest parameters){
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
}
