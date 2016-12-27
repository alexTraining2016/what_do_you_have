package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.RecipesByIngFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.QueryCreator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;
import comalexpolyanskyi.github.foodandhealth.sql.SQLQueryCreator;

public class RecipesByIngFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;

    private static final String SHORT_ART_T_NAME = " a";
    private static final String SATN_PLUS_DOT = SHORT_ART_T_NAME + ".";
    private static final String SHORT_ING_T_NAME = " i";
    private static final String SITN_PLUS_DOT = SHORT_ING_T_NAME + ".";

    public RecipesByIngFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new RecipesByIngFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);
        final QueryCreator creator = new SQLQueryCreator();
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_BY_INGREDIENT + parameters[0] + ApiConstants.API_BY_AUTH + parameters[1];
        final String select = SQLConstants.SELECT + creator.getSelectCreateQuery(Article.class, SQLConstants.SQL_ARTICLE_SELECT) +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + SHORT_ING_T_NAME +
                SQLConstants.ON + SITN_PLUS_DOT + ArticleIngredient.ARTICLE_ID + SQLConstants.EQ + SATN_PLUS_DOT + Article.ID +
                SQLConstants.WHERE + SITN_PLUS_DOT + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + parameters[0] + " )" +
                SQLConstants.GROUP_BY + SATN_PLUS_DOT + Article.ID +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(url, select), true, false);
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final QueryCreator creator = new SQLQueryCreator();
        final String select = SQLConstants.SELECT + creator.getSelectCreateQuery(Article.class, SQLConstants.SQL_ARTICLE_SELECT) +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + SHORT_ING_T_NAME +
                SQLConstants.ON + SITN_PLUS_DOT + ArticleIngredient.ARTICLE_ID + SQLConstants.EQ + SATN_PLUS_DOT + Article.ID +
                SQLConstants.WHERE + SITN_PLUS_DOT + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + searchParameter[0] + " )" +
                SQLConstants.AND + Article.SEARCH_NAME +
                SQLConstants.LIKE + "'%" + searchParameter[1].toLowerCase() + "%'" +
                SQLConstants.GROUP_BY + SATN_PLUS_DOT + Article.ID +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(null, select), false, true);
    }
}