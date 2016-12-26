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

public class AllRecipesFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";
    private static final String ALL_FOOD_RECIPES = "0";

    public AllRecipesFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_All + ApiConstants.API_BY_AUTH + parameters[0];
        final String whereParam = SQLConstants.WHERE + Article.TYPE
                + SQLConstants.BETWEEN + ALL_FOOD_RECIPES + SQLConstants.AND + ALL_DIET_RECIPES
                + SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, selectSql), false, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQLConstants.WHERE + Article.TYPE + SQLConstants.BETWEEN + ALL_FOOD_RECIPES
                + SQLConstants.AND + ALL_DIET_RECIPES
                + SQLConstants.AND + Article.SEARCH_NAME
                + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'"
                + SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;
        final String select = SQLConstants.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, select), false, true);
    }
}