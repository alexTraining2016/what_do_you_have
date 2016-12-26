package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.RecipesByIngFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;

public class RecipesByIngFragmentMediator extends BaseMediator<Cursor, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;

    private static final String SHORT_ART_T_NAME = " r";
    private static final String SATN_PLUS_DOT = SHORT_ART_T_NAME + ".";
    private static final String SATN_PL_D_Z = "," + SATN_PLUS_DOT;
    private static final String SHORT_ING_T_NAME = " i";
    private static final String SITN_PLUS_DOT = SHORT_ING_T_NAME + ".";

    public RecipesByIngFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new RecipesByIngFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_BY_INGREDIENT + parameters[0] + ApiConstants.API_BY_AUTH + parameters[1];
        final String select = SQLConstants.SELECT + SATN_PLUS_DOT + Article.ID + SATN_PL_D_Z + Article.AGING_TIME + SATN_PL_D_Z + Article.RECORDING_TIME
                + SATN_PL_D_Z + Article.NAME + SATN_PL_D_Z + Article.IMAGE_URI + SATN_PL_D_Z + Article.TYPE +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + SHORT_ING_T_NAME +
                SQLConstants.ON + SITN_PLUS_DOT + ArticleIngredient.ARTICLE_ID + " = " + SATN_PLUS_DOT + Article.ID +
                SQLConstants.WHERE + SITN_PLUS_DOT + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + parameters[0] + " )" +
                SQLConstants.GROUP_BY + SATN_PLUS_DOT + Article.ID;

        dao.get(new ParametersInformationRequest(url, select), true, false);
    }


    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String select = SQLConstants.SELECT + SATN_PLUS_DOT + Article.ID + SATN_PL_D_Z + Article.AGING_TIME + SATN_PL_D_Z +
                Article.RECORDING_TIME + SATN_PL_D_Z + Article.NAME + SATN_PL_D_Z + Article.IMAGE_URI + SATN_PL_D_Z + Article.TYPE +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + SHORT_ING_T_NAME +
                SQLConstants.ON + SITN_PLUS_DOT + ArticleIngredient.ARTICLE_ID + " = " + SATN_PLUS_DOT + Article.ID +
                SQLConstants.WHERE + SITN_PLUS_DOT + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + searchParameter[0] + " )" +
                SQLConstants.AND + Article.SEARCH_NAME +
                SQLConstants.LIKE + "'%" + searchParameter[1].toLowerCase() + "%'" +
                SQLConstants.GROUP_BY + SATN_PLUS_DOT + Article.ID +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(null, select), false, true);
    }
}