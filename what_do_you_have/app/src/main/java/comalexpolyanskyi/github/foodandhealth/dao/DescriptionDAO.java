package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;



public class DescriptionDAO extends BaseDAO<ArticleDO, ArticleDO> implements MVPContract.DAO<ParametersInformationRequest> {

    public DescriptionDAO(@NonNull MVPContract.RequiredPresenter<ArticleDO> presenter) {
        super(presenter);
    }

    @Override
    protected List<ContentValues> prepareContentValues(List<ArticleDO> result) {
        List<ContentValues> contentValuesList = new ArrayList<>(result.size());
        for (ArticleDO item : result) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ArticleDescription.ID, item.getId());
            contentValues.put(ArticleDescription.NAME, item.getName());
            contentValues.put(ArticleDescription.DESCRIPTION, item.getDescription());
            contentValues.put(ArticleDescription.LIKE_COUNT, item.getLikeCount());
            contentValues.put(ArticleDescription.REPOST_COUNT, item.getFavCount());
            contentValues.put(ArticleDescription.IMAGE_URI, item.getPhotoUrl());
            contentValues.put(ArticleDescription.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(ArticleDescription.AGING_TIME, 3600);
        }
        return contentValuesList;
    }

    @Override
    protected ArticleDO prepareResponse(Cursor cursor) {
        return convertToArticleDO(cursor);
    }

    private ArticleDO convertToArticleDO(Cursor cursor){
        if (cursor != null) {
            cursor.moveToFirst();
            ArticleDO request = null;
            try {
                request = new ArticleDO(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return request;
        }else{
            return null;
        }
    }

}
