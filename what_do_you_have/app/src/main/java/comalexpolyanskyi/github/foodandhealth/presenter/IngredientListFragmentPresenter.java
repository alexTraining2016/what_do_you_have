package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.IngredientItemDO;

public class IngredientListFragmentPresenter implements MVPContract.Presenter<Void>, MVPContract.RequiredPresenter<List<IngredientItemDO>> {

    private static final String REQUEST_URL = "sdda";
    private MVPContract.RequiredView<List<IngredientItemDO>> view;
    private MVPContract.DAO DAO;

    public IngredientListFragmentPresenter(@NonNull MVPContract.RequiredView view) {
        this.view = view;
        this.DAO = new IngredientListFragmentDAO(this);
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
        onSuccess(new ArrayList<IngredientItemDO>());
    }

    @Override
    public void onError() {
        view.showProgress(false);
        view.returnError("косяк");
    }

    @Override
    public void onSuccess(List<IngredientItemDO> response) {
        view.showProgress(false);
        char ch = 'a';
        for(int i = 0; i <= 100; i++){
            response.add(new IngredientItemDO(i, ch+"Ingredient Number " + i));
            if(i%3 == 0){
                ch ++;
            }
        }
        view.returnData(response);
    }
}
