package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;


public class TrainingListFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String TRAINING = "2";

    public TrainingListFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES + TRAINING + ApiConstants.API_BY_AUTH + parameters[0];
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class)
                + SQLConstants.WHERE + Article.TYPE + " = " + TRAINING;

        dao.get(new ParametersInformationRequest(url, selectSql), false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQLConstants.WHERE + Article.TYPE + " = " + TRAINING
                + SQLConstants.AND + Article.SEARCH_NAME
                + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, selectSql), false);
    }
}
