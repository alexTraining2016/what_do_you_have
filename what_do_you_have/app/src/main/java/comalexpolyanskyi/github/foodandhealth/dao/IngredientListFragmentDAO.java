package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.base.Charsets;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class IngredientListFragmentDAO extends AbstractDAO<Cursor> {

    public IngredientListFragmentDAO(@NonNull MVPContract.RequiredPresenter<Cursor> presenter) {
        super(presenter);
    }

    @Override
    protected Cursor update(ParametersInformationRequest parameters) {
        byte [] requestBytes = httpClient.loadDataFromHttp(parameters.getUrl(), true);
        if(requestBytes != null) {
            String requestString = new String(requestBytes, Charsets.UTF_8);
            Log.e("123", requestString);
            Type listType = new TypeToken<List<IngredientItemDO>>() {}.getType();
            Gson gson = new GsonBuilder().create();
            final List<IngredientItemDO> requestList = gson.fromJson(requestString, listType);
            saveToCache(requestList);
            return getFromCache(parameters.getSelectParameters());
        }else{
            return null;
        }
    }

    private void saveToCache(List<IngredientItemDO> requestList) {
        List<ContentValues> contentValuesList = new ArrayList<>(requestList.size());
        for (IngredientItemDO item : requestList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Ingredient.ID, item.getId());
            contentValues.put(Ingredient.NAME, item.getName());
            contentValues.put(Ingredient.RECORDING_TIME, System.currentTimeMillis());
            contentValues.put(Ingredient.AGING_TIME, 600);
            contentValuesList.add(contentValues);
        }
        operations.bulkUpdate(Ingredient.class, contentValuesList);
    }

    @Override
    protected Cursor getFromCache(String[] parameters) {
        return operations.query(parameters[0] + DBHelper.getTableName(Ingredient.class) + parameters[1]);
    }
}

