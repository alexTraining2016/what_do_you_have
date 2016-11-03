package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentPresenter implements IMVPContract.Presenter<QueryParameters>, IMVPContract.RequiredPresenter<List<ArticleListItemDO>> {

    private static final String REQUEST_URL = "sdda";
    private IMVPContract.RequiredView<List<ArticleListItemDO>> view;
    private IMVPContract.DAO<ParametersInformationRequest> DAO;
    private ParametersInformationRequest parametersInformationRequest;

    public ArticleListFragmentPresenter(@NonNull IMVPContract.RequiredView<List<ArticleListItemDO>> view) {
        this.view = view;
        this.DAO = new ArticleListFragmentDAO(this);
    }

    @Override
    public void onConfigurationChanged(IMVPContract.RequiredView view) {
        this.view = view;
    }


    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadData(QueryParameters parameters) {
        view.showProgress(true);
        String url = null;
        HashMap<String, String> selectParam = new HashMap<>();
        switch (parameters.getViewType()){
            case ArticlesTypeRequest.ALL_FOOD_RECIPES:
                url = Api.API_BASE_URL+ Api.API_ARTICLES;
                selectParam.put(Article.TYPE, " WHERE " + Article.TYPE + "=" + Article.TYPE);
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
        parametersInformationRequest = new ParametersInformationRequest(url, selectParam, null, null, null);
        DAO.get(parametersInformationRequest);
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
        DAO.put(parametersInformationRequest);
        view.returnData(response);
    }

}