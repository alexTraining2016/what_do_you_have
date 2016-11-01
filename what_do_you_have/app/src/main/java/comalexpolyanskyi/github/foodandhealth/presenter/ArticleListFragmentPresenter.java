package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.ArticleListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentPresenter implements IMVPContract.Presenter<QueryParameters>, IMVPContract.RequiredPresenter<List<ArticleListItemDO>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView view;
    private IMVPContract.Model model;

    public ArticleListFragmentPresenter(@NonNull IMVPContract.RequiredView<List<ArticleListItemDO>> view) {
        this.view = view;
        this.model = new ArticleListFragmentModel(this);
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
            case ArticlesTypeRequest.ALL_FOOD_RECIPES:
                url = Api.API_BASE_URL+Api.API_ARTICLES;
                break;
            case ArticlesTypeRequest.FOOD_RECIPES_BY_INGREDIENT:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.FAVORITES_FOOD_RECIPES:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.ALL_DIET_RECIPES:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.ALL_TRAINING_RECIPES:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.FAVORITES_TRAINING_AND_DIET_RECIPES:
                url = REQUEST_URL;
                break;
        }
        model.getData(url);
    }

    @Override
    public void onError() {
        if(view != null){
            view.showProgress(false);
            view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
        }
    }

    @Override
    public void onSuccess(List<ArticleListItemDO> response) {
        view.showProgress(false);
        view.returnData(response);
    }

}