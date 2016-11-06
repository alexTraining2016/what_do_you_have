package comalexpolyanskyi.github.foodandhealth.dao;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.presenter.IngredientListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class IngredientListFragmentDAO implements MVPContract.DAO<ParametersInformationRequest> {

    public IngredientListFragmentDAO(IngredientListFragmentPresenter ingredientListFragmentPresenter) {
    }

    @Override
    public void get(ParametersInformationRequest parameters) {

    }
}
