package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;


public class DescriptionActivityPresenter extends BasePresenter<ArticleDO, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;

    public DescriptionActivityPresenter(@NonNull MVPContract.RequiredView<ArticleDO> view) {
        super(view);

        dao = new DescriptionDAO(this);
    }

    @Override
    public void loadData(String... parameters) {

        final boolean isNeedForceUpdate = parameters[2] != null;
        String act = "";
        if(isNeedForceUpdate) {
            act = parameters[2];
        }else{
            super.loadData(parameters);
        }
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_DESC + parameters[0] + ApiConstants.API_BY_AUTH + parameters[1]+act;
        final String whereParam = SQLConstants.WHERE + Article.ID + " = " + parameters[0];
        final String select = SQLConstants.S_F + DBHelper.getTableName(ArticleDescription.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, select), isNeedForceUpdate);
    }
}
