package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.ListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.models.pojo.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.ui.activities.MainActivity;
import comalexpolyanskyi.github.foodandhealth.utils.ContextHolder;

public class ListFragmentPresenter implements IMVPContract.Presenter<QueryParameters>, IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemBean>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView view;
    private IMVPContract.Model model;

    public ListFragmentPresenter(@NonNull IMVPContract.RequiredView view) {
        this.view = view;
        this.model = new ListFragmentModel(this);
    }

    @Override
    public void onConfigurationChanged(IMVPContract.RequiredView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
        model.onDestroy();
    }

    @Override
    public void loadData(QueryParameters parameters) {
        view.showProgress(true);
        String url = null;
        switch (parameters.getViewType()){
            case MainActivity.ALL_FOOD_RECIPES:
                url = REQUEST_URL;
                break;
            case MainActivity.FOOD_RECIPES_BY_INGREDIENT:
                url = REQUEST_URL;
                break;
            case MainActivity.FAVORITES_FOOD_RECIPES:
                url = REQUEST_URL;
                break;
            case MainActivity.ALL_DIET_RECIPES:
                url = REQUEST_URL;
                break;
            case MainActivity.ALL_TRAINING_RECIPES:
                url = REQUEST_URL;
                break;
            case MainActivity.FAVORITES_TRAINING_AND_DIET_RECIPES:
                url = REQUEST_URL;
                break;
        }
        model.getData(url);
    }

    @Override
    public void onError() {
        view.showProgress(false);
        view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
    }

    @Override
    public void onSuccess(SparseArrayCompat<ListItemBean> response) {
        view.showProgress(false);
    }
}