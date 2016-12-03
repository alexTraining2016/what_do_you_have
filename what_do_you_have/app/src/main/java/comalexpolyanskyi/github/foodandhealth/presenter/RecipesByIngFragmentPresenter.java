package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.RecipesByIngFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;

public class RecipesByIngFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;

    public RecipesByIngFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new RecipesByIngFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_BY_INGREDIENT + parameters[0] + ApiConstants.API_BY_AUTH + parameters[1];
        final String select = SQLConstants.SELECT + "r." + Article.ID + ",r." + Article.AGING_TIME + ",r." + Article.RECORDING_TIME + ",r." + Article.NAME + ",r." + Article.IMAGE_URI + ",r." + Article.TYPE +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + " r" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + " i" + SQLConstants.ON + "i." + ArticleIngredient.ARTICLE_ID + " = r." + Article.ID +
                SQLConstants.WHERE + "i." + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + parameters[0] + " )" +
                SQLConstants.GROUP_BY + "r." + Article.ID;

        dao.get(new ParametersInformationRequest(url, select), true);
    }


    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String select = SQLConstants.SELECT + "r." + Article.ID + ",r." + Article.AGING_TIME + ",r." + Article.RECORDING_TIME + ",r." + Article.NAME + ",r." + Article.IMAGE_URI + ",r." + Article.TYPE +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + " r" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(ArticleIngredient.class) + " i" + SQLConstants.ON + "i." + ArticleIngredient.ARTICLE_ID + " = r." + Article.ID +
                SQLConstants.WHERE + "i." + ArticleIngredient.INGREDIENT_ID + SQLConstants.IN + "( " + searchParameter[0] + " )" +
                SQLConstants.AND + Article.SEARCH_NAME +
                SQLConstants.LIKE + "'%" + searchParameter[1].toLowerCase() + "%'" +
                SQLConstants.GROUP_BY + "r." + Article.ID;

        dao.get(new ParametersInformationRequest(null, select), false);
    }
}