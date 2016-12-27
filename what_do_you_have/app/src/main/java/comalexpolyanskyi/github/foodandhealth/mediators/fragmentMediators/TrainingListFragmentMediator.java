package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;

public class TrainingListFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;
    private static final String TRAINING = "2";

    public TrainingListFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES + TRAINING + ApiConstants.API_BY_AUTH + parameters[0];
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class)
                + SQLConstants.WHERE + Article.TYPE + SQLConstants.EQ + TRAINING + SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(url, selectSql), false, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQLConstants.WHERE + Article.TYPE + SQLConstants.EQ + TRAINING
                + SQLConstants.AND + Article.SEARCH_NAME
                + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + where +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(null, selectSql), false, true);
    }
}
