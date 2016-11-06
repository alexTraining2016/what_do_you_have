package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;


public class DescriptionActivityPresenter implements  MVPContract.Presenter<String>, MVPContract.RequiredPresenter<ArticleDO> {

    private MVPContract.RequiredView<ArticleDO> view;
    private MVPContract.DAO<ParametersInformationRequest> dao;
    private ParametersInformationRequest parametersInformationRequest;

    public DescriptionActivityPresenter(@NonNull MVPContract.RequiredView<ArticleDO> view) {
        this.view = view;
        this.dao = new DescriptionDAO(this);
    }

    @Override
    public void onConfigurationChanged(MVPContract.RequiredView view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadData(String parameters) {
        String url = Api.API_BASE_URL+Api.API_ARTICLES_DESC+parameters;
        String selectParam = "SELECT * FROM ";
        String whereParam = " WHERE " + Article.ID + " = " + parameters;
        String[] select = {selectParam, whereParam};
        dao.get(new ParametersInformationRequest(url, select, null));
    }

    @Override
    public void onError() {
        if(view != null){
            view.showProgress(false);
            view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
        }
    }

    @Override
    public void onSuccess(ArticleDO response) {
        if(view != null){
            view.showProgress(false);
            view.returnData(response);
        }
    }
}
