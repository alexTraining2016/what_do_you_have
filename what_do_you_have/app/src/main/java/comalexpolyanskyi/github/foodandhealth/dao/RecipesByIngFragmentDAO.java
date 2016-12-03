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

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleByIngredientDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.IngredientIdDO;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;


public class RecipesByIngFragmentDAO extends BaseDAO<Cursor> {

    public RecipesByIngFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkUpdate(Article.class, contentValuesList);
    }

    @Override
    protected List<ContentValues> processRequest(@NonNull String request) {
        final List<ContentValues> contentValuesList = new ArrayList<>();

        try {
            final Type type = new TypeToken<List<ArticleByIngredientDO>>() {
            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<ArticleByIngredientDO> result = gson.fromJson(request, type);

            for (ArticleByIngredientDO item : result) {
                ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        } catch (Exception e) {
            return null;
        }

        return contentValuesList;
    }

    @Override
    protected Cursor prepareResponse(@NonNull Cursor cursor) {
        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }

    private void saveJunctionTable(List<IngredientIdDO> items, int articleId) {
        final List<ContentValues> contentList = new ArrayList<>();

        for (IngredientIdDO item : items) {
            final ContentValues content = new ContentValues();
            content.put(ArticleIngredient.ID, item.getId());
            content.put(ArticleIngredient.ARTICLE_ID, articleId);
            content.put(ArticleIngredient.INGREDIENT_ID, item.getIngredientId());
            contentList.add(content);
        }
        operations.bulkUpdate(ArticleIngredient.class, contentList);
    }

    private ContentValues prepareContentValues(ArticleByIngredientDO item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Article.ID, item.getId());
        contentValues.put(Article.TYPE, item.getType());
        contentValues.put(Article.NAME, item.getName());
        contentValues.put(Article.SEARCH_NAME, item.getName().toLowerCase());
        contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
        contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis()/1000);
        contentValues.put(Article.AGING_TIME, 3600);
        saveJunctionTable(item.getIngredientsId(), item.getId());

        return contentValues;
    }
}
