package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class IngredientListFragmentDAO extends BaseDAO<Cursor, IngredientItemDO> {

    public IngredientListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
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
    protected List<ContentValues> prepareContentValues(List<IngredientItemDO> result) {
        List<ContentValues> contentValuesList = new ArrayList<>(result.size());
        for (IngredientItemDO item : result) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Ingredient.ID, item.getId());
            contentValues.put(Ingredient.NAME, item.getName());
            contentValues.put(Ingredient.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Ingredient.AGING_TIME, 600);
            contentValuesList.add(contentValues);
        }
        return contentValuesList;
    }
}

