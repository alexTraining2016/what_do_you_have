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

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class IngredientListFragmentDAO extends BaseDAO<Cursor> {

    public IngredientListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected void saveToCache(List<ContentValues> contentValuesList) {
        operations.bulkUpdate(Ingredient.class, contentValuesList);
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
            final Type type = new TypeToken<List<IngredientItemDO>>() {
            }.getType();
            final Gson gson = new GsonBuilder().create();
            final List<IngredientItemDO> result = gson.fromJson(request, type);

            for (IngredientItemDO item : result) {
                ContentValues contentValue = prepareContentValues(item);
                contentValuesList.add(contentValue);
            }
        } catch (Exception e) {
            return null;
        }

        return contentValuesList;
    }

    private ContentValues prepareContentValues(IngredientItemDO item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Ingredient.ID, item.getId());
        contentValues.put(Ingredient.NAME, item.getName());
        contentValues.put(Ingredient.RECORDING_TIME, System.currentTimeMillis());
        contentValues.put(Ingredient.AGING_TIME, 600);

        return contentValues;
    }
}

