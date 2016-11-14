package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class ArticleListFragmentDAO extends BaseDAO<Cursor, ArticleListItemDO> implements MVPContract.DAO<ParametersInformationRequest> {

    public ArticleListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected Cursor prepareResponse(Cursor cursor) {
        if(cursor.getCount() > 0){
            return cursor;
        }else{
            return null;
        }
    }

    @Override
    protected List<ContentValues> prepareContentValues(List<ArticleListItemDO> result) {
        List<ContentValues> contentValuesList = new ArrayList<>(result.size());
        for (ArticleListItemDO item : result) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Article.ID, item.getId());
            contentValues.put(Article.TYPE, item.getType());
            contentValues.put(Article.NAME, item.getName());
            contentValues.put(Article.SEARCH_NAME, item.getName().toLowerCase());
            contentValues.put(Article.IMAGE_URI, item.getPhotoUrl());
            contentValues.put(Article.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Article.AGING_TIME, 3600);
            contentValuesList.add(contentValues);
        }
        return contentValuesList;
    }
}
