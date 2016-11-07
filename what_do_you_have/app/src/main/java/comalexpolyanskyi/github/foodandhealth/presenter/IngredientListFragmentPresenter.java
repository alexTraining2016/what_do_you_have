package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class IngredientListFragmentPresenter implements MVPContract.Presenter<Void>, MVPContract.RequiredPresenter<Cursor> {

    private MVPContract.RequiredView<Cursor> view;
    private MVPContract.DAO<ParametersInformationRequest> dao;

    public IngredientListFragmentPresenter(@NonNull MVPContract.RequiredView view) {
        this.view = view;
        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void onConfigurationChanged(MVPContract.RequiredView view) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadData(Void parameters) {
        view.showProgress(true);
        String url = Api.API_BASE_URL+Api.API_ALL_INGREDIENT;
        String selectFrom = "SELECT * FROM ";
        String where = " ORDER BY "+ Ingredient.NAME;
        String[] select = {selectFrom, where};
        dao.get(new ParametersInformationRequest(url, select, null));
    }

    @Override
    public void onError() {
        view.showProgress(false);
        view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
    }

    @Override
    public void onSuccess(Cursor response) {
        view.showProgress(false);

        view.returnData(response);
    }
}
