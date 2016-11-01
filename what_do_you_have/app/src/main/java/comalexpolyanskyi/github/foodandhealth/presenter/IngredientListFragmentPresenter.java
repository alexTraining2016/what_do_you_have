package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.models.IngredientListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.IngredientItemDO;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragmentPresenter implements IMVPContract.Presenter<Void>, IMVPContract.RequiredPresenter<List<IngredientItemDO>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView<List<IngredientItemDO>> view;
    private IMVPContract.Model model;

    public IngredientListFragmentPresenter(@NonNull IMVPContract.RequiredView view) {
        this.view = view;
        this.model = new IngredientListFragmentModel(this);
    }

    @Override
    public void onConfigurationChanged(IMVPContract.RequiredView view) {

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
