package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;

public class RecipesByKindFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;

    public RecipesByKindFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_All + ApiConstants.API_BY_KIND + parameters[0];
        final String whereParam = SQLConstants.WHERE + Article.KIND + SQLConstants.EQ + parameters[0] +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, selectSql), false, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String select = SQLConstants.S_F + DBHelper.getTableName(Article.class);
        final String where = SQLConstants.WHERE + Article.KIND + SQLConstants.EQ + searchParameter[1] + SQLConstants.AND +
                Article.SEARCH_NAME + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'" +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(null, select + where), false, true);
    }
}