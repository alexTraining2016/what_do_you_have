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

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;


public class DescriptionDAO extends BaseDAO<ArticleDO> implements InteractionContract.DAO<ParametersInformationRequest> {

    public DescriptionDAO(@NonNull InteractionContract.RequiredPresenter<ArticleDO> presenter) {
        super(presenter);
    }

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkUpdate(ArticleDescription.class, contentValuesList);
    }

    @Override
    protected List<ContentValues> processRequest(@NonNull String request) {
        final List<ContentValues> contentValuesList = new ArrayList<>();

        try {
            final Type type = new TypeToken<List<ArticleDO>>() {
            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<ArticleDO> result = gson.fromJson(request, type);

            for (ArticleDO item : result) {
                final ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        } catch (Exception e) {
            return null;
        }

        return contentValuesList;
    }

    private ContentValues prepareContentValues(ArticleDO item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleDescription.ID, item.getId());
        contentValues.put(ArticleDescription.NAME, item.getName());
        contentValues.put(ArticleDescription.DESCRIPTION, item.getDescription());
        contentValues.put(ArticleDescription.LIKE_COUNT, item.getLikeCount());
        contentValues.put(ArticleDescription.REPOST_COUNT, item.getFavCount());
        contentValues.put(ArticleDescription.IMAGE_URI, item.getPhotoUrl());
        contentValues.put(ArticleDescription.USER_ID, item.getUserId());
        contentValues.put(ArticleDescription.IS_LIKE, item.getLike());
        contentValues.put(ArticleDescription.IS_REPOST, item.getRepost());
        contentValues.put(ArticleDescription.RECORDING_TIME, System.currentTimeMillis()/1000);
        contentValues.put(ArticleDescription.AGING_TIME, 3600);

        return contentValues;
    }

    @Override
    protected ArticleDO prepareResponse(@NonNull Cursor cursor) {
        return convertToArticleDO(cursor);
    }

    private ArticleDO convertToArticleDO(@NonNull Cursor cursor) {
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ArticleDO request = null;

            try {
                request = new ArticleDO(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return request;
        } else {
            return null;
        }
    }

}
