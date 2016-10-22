package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.models.IngredientListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragmentPresenter implements IMVPContract.Presenter<Void>, IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemBean>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView view;
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

    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(SparseArrayCompat<ListItemBean> response) {

    }

}
