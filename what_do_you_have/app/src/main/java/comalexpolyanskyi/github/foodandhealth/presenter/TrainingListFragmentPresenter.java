package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;


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

        final String url = Api.API_BASE_URL + Api.API_ARTICLES + TRAINING + Api.API_BY_AUTH + parameters[0];
        final String selectSql = SQL.S_F + DBHelper.getTableName(Article.class)
                + SQL.WHERE + Article.TYPE + " = " + TRAINING;

        dao.get(new ParametersInformationRequest(url, selectSql, null), false, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQL.WHERE + Article.TYPE + " = " + TRAINING
                + SQL.AND + Article.SEARCH_NAME
                + SQL.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String selectSql = SQL.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, selectSql, null), true, false);
    }
}
