package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.RecipesByIngFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;

public class RecipesByIngFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";
    private static final String ALL_FOOD_RECIPES = "0";

    public RecipesByIngFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new RecipesByIngFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = Api.API_BASE_URL + Api.API_BY_INGREDIENT + parameters[0] + Api.API_BY_AUTH + parameters[1];
        final String select = SQL.SELECT + "r." + Article.ID + ",r." + Article.AGING_TIME + ",r." + Article.RECORDING_TIME + ",r." + Article.NAME + ",r." + Article.IMAGE_URI + ",r." + Article.TYPE +
                SQL.FROM + DBHelper.getTableName(Article.class) + " r" +
                SQL.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + " i" + SQL.ON + "i." + ArticleIngredient.ARTICLE_ID + " = r." + Article.ID +
                SQL.WHERE + "i." + ArticleIngredient.INGREDIENT_ID + SQL.IN + "( " + parameters[0] + " )" +
                SQL.GROUP_BY + "r." + Article.ID;

        dao.get(new ParametersInformationRequest(url, select, null), false, true);
    }


    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String select = SQL.SELECT + "r." + Article.ID + ",r." + Article.AGING_TIME + ",r." + Article.RECORDING_TIME + ",r." + Article.NAME + ",r." + Article.IMAGE_URI + ",r." + Article.TYPE +
                SQL.FROM + DBHelper.getTableName(Article.class) + " r" +
                SQL.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + " i" + SQL.ON + "i." + ArticleIngredient.ARTICLE_ID + " = r." + Article.ID +
                SQL.WHERE + "i." + ArticleIngredient.INGREDIENT_ID + SQL.IN + "( " + searchParameter[0] + " )" +
                SQL.AND + Article.SEARCH_NAME +
                SQL.LIKE + "'%" + searchParameter[1].toLowerCase() + "%'" +
                SQL.GROUP_BY + "r." + Article.ID;

        dao.get(new ParametersInformationRequest(null, select, null), true, false);
    }
}