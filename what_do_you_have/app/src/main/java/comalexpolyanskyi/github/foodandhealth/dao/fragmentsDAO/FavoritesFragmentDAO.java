package comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.baseFragmentsDAO.BaseDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.BindingUserDataDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.FavArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;

public class FavoritesFragmentDAO extends BaseDAO<Cursor> implements InteractionContract.DAO<ParametersInformationRequest> {

    public FavoritesFragmentDAO(@NonNull InteractionContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkInsert(Article.class, contentValuesList);
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
