package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;


public class DescriptionActivityPresenter extends BasePresenter<ArticleDO, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;

    public DescriptionActivityPresenter(@NonNull MVPContract.RequiredView<ArticleDO> view) {
        super(view);
        dao = new DescriptionDAO(this);
    }

    @Override
    public void loadData(String parameters) {
        super.loadData(parameters);
        String url = Api.API_BASE_URL+Api.API_ARTICLES_DESC+parameters;
        String selectParam = "SELECT * FROM ";
        String whereParam = " WHERE " + Article.ID + " = " + parameters;
        String select = selectParam + DBHelper.getTableName(ArticleDescription.class) + whereParam;
        dao.get(new ParametersInformationRequest(url, select, null), false);
    }
}
