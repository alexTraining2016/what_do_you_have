package comalexpolyanskyi.github.foodandhealth.mediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;


public class DietsRecipesListFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";

    public DietsRecipesListFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES + ALL_DIET_RECIPES + ApiConstants.API_BY_AUTH + parameters[0];
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class)
                + SQLConstants.WHERE + Article.TYPE + "=" + ALL_DIET_RECIPES;

        dao.get(new ParametersInformationRequest(url, selectSql), false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQLConstants.WHERE + Article.TYPE + "=" + ALL_DIET_RECIPES
                + SQLConstants.AND + Article.SEARCH_NAME
                + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String selectSql = SQLConstants.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, selectSql), false);
    }
}
