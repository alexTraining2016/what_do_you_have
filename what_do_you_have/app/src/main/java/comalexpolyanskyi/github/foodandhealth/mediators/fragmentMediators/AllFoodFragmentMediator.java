package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.KindFood;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;

public class AllFoodFragmentMediator extends BaseMediator<Cursor, String> {

    public AllFoodFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_KIND;
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(KindFood.class);

        dao.get(new ParametersInformationRequest(url, selectSql), false, false);
    }

    @Override
    public void search(String... searchParameter) {
    }
}
