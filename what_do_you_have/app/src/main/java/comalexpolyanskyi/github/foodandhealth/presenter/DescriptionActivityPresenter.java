package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
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
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final boolean isNeedForceUpdate = parameters[2] != null;
        final String url = Api.API_BASE_URL + Api.API_ARTICLES_DESC + parameters[0] + Api.API_BY_AUTH + parameters[1];
        final String whereParam = SQL.WHERE + Article.ID + " = " + parameters[0];
        final String select = SQL.S_F + DBHelper.getTableName(ArticleDescription.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, select, null), false, isNeedForceUpdate);
    }
}
