package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;


public class IngredientListFragmentPresenter extends BasePresenter<Cursor, String>{

    private MVPContract.DAO<ParametersInformationRequest> dao;

    public IngredientListFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = Api.API_BASE_URL+Api.API_ALL_INGREDIENT + Api.API_BY_AUTH + parameters[0];
        final String where = " ORDER BY "+ Ingredient.NAME;
        final String select = SQL.S_F + DBHelper.getTableName(Ingredient.class) + where;

        dao.get(new ParametersInformationRequest(url, select, null), false, false);
    }
}
