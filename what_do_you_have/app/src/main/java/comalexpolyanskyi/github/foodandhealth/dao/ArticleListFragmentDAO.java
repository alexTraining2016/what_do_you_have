package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

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

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkUpdate(Article.class, contentValuesList);
    }

    @Override
    protected Cursor prepareResponse(@NonNull Cursor cursor) {
        if (cursor.getCount() > 0) {
            return cursor;
        }else{
            return null;
        }
    }

    @Override
    protected List<ContentValues> processRequest(String request){
        List<ContentValues> contentValuesList = new ArrayList<>();
        try {
            Type type = new TypeToken<List<ArticleListItemDO>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            final List<ArticleListItemDO> result = gson.fromJson(request, type);
            for (ArticleListItemDO item : result) {
                ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        }catch (Exception e){
            return null;
        }
        return contentValuesList;
    }

    private ContentValues prepareContentValues(ArticleListItemDO item) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Article.ID, item.getId());
            contentValues.put(Article.TYPE, item.getType());
            contentValues.put(Article.NAME, item.getName());
            contentValues.put(Article.SEARCH_NAME, item.getName().toLowerCase());
            contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
            contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Article.AGING_TIME, 3600);
        return contentValues;
    }
}
