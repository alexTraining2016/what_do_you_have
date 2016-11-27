package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;


public class DietsRecipesListFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";

    public DietsRecipesListFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = Api.API_BASE_URL + Api.API_ARTICLES + ALL_DIET_RECIPES + Api.API_BY_AUTH + parameters[0];
        final String selectSql = SQL.S_F + DBHelper.getTableName(Article.class)
                + SQL.WHERE + Article.TYPE + "=" + ALL_DIET_RECIPES;

        dao.get(new ParametersInformationRequest(url, selectSql, null), false, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQL.WHERE + Article.TYPE + "=" + ALL_DIET_RECIPES
                + SQL.AND + Article.SEARCH_NAME
                + SQL.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String selectSql = SQL.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, selectSql, null), true, false);
    }
}