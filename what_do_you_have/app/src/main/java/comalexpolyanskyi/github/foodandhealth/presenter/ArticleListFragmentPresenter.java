package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class ArticleListFragmentPresenter implements MVPContract.Presenter<QueryParameters>, MVPContract.RequiredPresenter<Cursor> {

    private static final String REQUEST_URL = "sdda";
    private MVPContract.RequiredView<Cursor> view;
    private MVPContract.DAO<ParametersInformationRequest> DAO;
    private ParametersInformationRequest parametersInformationRequest;

    public ArticleListFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        this.view = view;
        this.DAO = new ArticleListFragmentDAO(this);
    }

    @Override
    public void onConfigurationChanged(MVPContract.RequiredView view)
    {
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
        String selectParam = "SELECT * FROM ";
        String whereParam = null;
        switch (parameters.getViewType()){
            case ArticlesTypeRequest.ALL_FOOD_RECIPES:
                url = Api.API_BASE_URL+ Api.API_ARTICLES_All;
                whereParam = " WHERE " + Article.TYPE + " BETWEEN " + ArticlesTypeRequest.ALL_FOOD_RECIPES + " AND " + ArticlesTypeRequest.ALL_DIET_RECIPES;
                break;
            case ArticlesTypeRequest.FOOD_RECIPES_BY_INGREDIENT:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.FAVORITES_FOOD_RECIPES:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.ALL_DIET_RECIPES:
                url = Api.API_BASE_URL+ Api.API_ARTICLES+ArticlesTypeRequest.ALL_DIET_RECIPES;
                whereParam =  " WHERE " + Article.TYPE + "=" + parameters.getViewType();
                break;
            case ArticlesTypeRequest.ALL_TRAINING_RECIPES:
                url = Api.API_BASE_URL+ Api.API_ARTICLES+ArticlesTypeRequest.ALL_TRAINING_RECIPES;
                whereParam = " WHERE " + Article.TYPE + "=" + parameters.getViewType();
                break;
            case ArticlesTypeRequest.FAVORITES_TRAINING_AND_DIET_RECIPES:
                url = REQUEST_URL;
                break;
        }
        String[] select = {selectParam, whereParam};
        parametersInformationRequest = new ParametersInformationRequest(url, select, null);
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
    public void onSuccess(Cursor cursor) {
        if(view != null) {
            view.showProgress(false);
            view.returnData(cursor);
        }
    }
}