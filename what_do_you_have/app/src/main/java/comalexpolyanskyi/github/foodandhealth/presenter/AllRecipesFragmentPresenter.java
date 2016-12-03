package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;

public class AllRecipesFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";
    private static final String ALL_FOOD_RECIPES = "0";

    public AllRecipesFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_All + ApiConstants.API_BY_AUTH + parameters[0];
        final String whereParam = SQLConstants.WHERE + Article.TYPE
                + SQLConstants.BETWEEN + ALL_FOOD_RECIPES + SQLConstants.AND + ALL_DIET_RECIPES;
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, selectSql), false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQLConstants.WHERE + Article.TYPE + SQLConstants.BETWEEN + ALL_FOOD_RECIPES
                + SQLConstants.AND + ALL_DIET_RECIPES
                + SQLConstants.AND + Article.SEARCH_NAME
                + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String select = SQLConstants.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, select), false);
    }
}