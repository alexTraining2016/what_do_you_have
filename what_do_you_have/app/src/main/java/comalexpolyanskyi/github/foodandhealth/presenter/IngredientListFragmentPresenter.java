package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.models.IngredientListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.pojo.IngredientItemModel;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragmentPresenter implements IMVPContract.Presenter<Void>, IMVPContract.RequiredPresenter<List<IngredientItemModel>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView<List<IngredientItemModel>> view;
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
        onSuccess(new ArrayList<IngredientItemModel>());
    }

    @Override
    public void onError() {
        view.showProgress(false);
        view.returnError("косяк");
    }

    @Override
    public void onSuccess(List<IngredientItemModel> response) {
        view.showProgress(false);

        for(int i = 0; i <= 100; i++){
            Log.i("123", "1234");
            response.add(new IngredientItemModel(i, "Ingredient Number " + i));
        }
        view.returnData(response);
    }

}
