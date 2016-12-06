package comalexpolyanskyi.github.foodandhealth.mediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;


public class IngredientListFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;

    public IngredientListFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL+ ApiConstants.API_ALL_INGREDIENT + ApiConstants.API_BY_AUTH + parameters[0];
        final String where = " ORDER BY "+ Ingredient.NAME;
        final String select = SQLConstants.S_F + DBHelper.getTableName(Ingredient.class) + where;

        dao.get(new ParametersInformationRequest(url, select), false);
    }
}
