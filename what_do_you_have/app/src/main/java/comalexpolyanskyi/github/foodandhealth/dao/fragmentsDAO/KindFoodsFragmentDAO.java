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
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.KindFoodDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.KindFood;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;

public class KindFoodsFragmentDAO extends BaseDAO<Cursor> implements InteractionContract.DAO<ParametersInformationRequest> {

    public KindFoodsFragmentDAO(@NonNull InteractionContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkInsert(KindFood.class, contentValuesList);
    }

    @Override
    protected List<ContentValues> processRequest(@NonNull String request) {
        final List<ContentValues> contentValuesList = new ArrayList<>();
        try {
            final Type type = new TypeToken<List<KindFoodDO>>() {

            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<KindFoodDO> result = gson.fromJson(request, type);
            for (KindFoodDO item : result) {
                final ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        } catch (Exception e) {
            return null;
        }

        return contentValuesList;
    }

    private ContentValues prepareContentValues(KindFoodDO item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(KindFood.ID, item.getId());
        contentValues.put(KindFood.NAME, item.getName());
        contentValues.put(KindFood.RECORDING_TIME, System.currentTimeMillis() / 1000);
        contentValues.put(KindFood.AGING_TIME, 3600);

        return contentValues;
    }

    @Override
    protected Cursor prepareResponse(@NonNull Cursor cursor) {
        if (cursor.getCount() > 0) {
            return cursor;
        } else {
            return null;
        }
    }
}
