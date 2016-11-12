package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;


public class IngredientListFragmentPresenter extends BasePresenter<Cursor, Void>{

    private MVPContract.DAO<ParametersInformationRequest> dao;

    public IngredientListFragmentPresenter(@NonNull MVPContract.RequiredView view) {
        super(view);
        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(Void parameters) {
        super.loadData(parameters);
        String url = Api.API_BASE_URL+Api.API_ALL_INGREDIENT;
        String selectFrom = "SELECT * FROM ";
        String where = " ORDER BY "+ Ingredient.NAME;
        String select = selectFrom + DBHelper.getTableName(Ingredient.class) + where;
        dao.get(new ParametersInformationRequest(url, select, null), false);
    }
}
