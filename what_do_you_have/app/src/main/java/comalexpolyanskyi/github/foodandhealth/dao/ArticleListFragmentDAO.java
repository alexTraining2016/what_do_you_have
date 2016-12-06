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
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;

public class ArticleListFragmentDAO extends BaseDAO<Cursor> implements InteractionContract.DAO<ParametersInformationRequest> {

    public ArticleListFragmentDAO(@NonNull InteractionContract.RequiredPresenter<Cursor> presenter) {
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
        } else {
            return null;
        }
    }

    @Override
    protected List<ContentValues> processRequest(@NonNull String request) {
        final List<ContentValues> contentValuesList = new ArrayList<>();

        try {
            final Type type = new TypeToken<List<ArticleListItemDO>>(){
            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<ArticleListItemDO> result = gson.fromJson(request, type);
            for (ArticleListItemDO item : result) {
                final ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        } catch (Exception e) {
            return null;
        }

        return contentValuesList;
    }

    private ContentValues prepareContentValues(ArticleListItemDO item) {
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
}
