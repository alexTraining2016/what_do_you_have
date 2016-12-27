package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleIngredient;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.FavoritesFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.QueryCreator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;
import comalexpolyanskyi.github.foodandhealth.sql.SQLQueryCreator;

public class FavoritesFragmentMediator extends BaseMediator<Cursor, String> {

    public FavoritesFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);
        this.dao = new FavoritesFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        final QueryCreator creator = new SQLQueryCreator();
        final String select = SQLConstants.SELECT + creator.getSelectCreateQuery(Article.class, SQLConstants.SQL_ARTICLE_SELECT) +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + " a" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + " f " + SQLConstants.ON + " a." + Article.ID + " = f." + Favorites.ART_ID +
                SQLConstants.WHERE + "(f." + Favorites.USER_ID + SQLConstants.EQ + parameters[1] + ") " +
                SQLConstants.AND + "(a." + Article.TYPE + SQLConstants.EQ + parameters[2] + ")" +
                SQLConstants.AND + "(f." + Favorites.ISFAVORITES + " = 1)" +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_FOR_FAVORITES + parameters[2] + ApiConstants.API_BY_AUTH + parameters[0];

        if (parameters.length > 3) {
            dao.update(new ParametersInformationRequest(url + ApiConstants.API_REMOVE_FROM_FAVORITES + parameters[3], select));
        } else {
            super.loadData(parameters);
            dao.get(new ParametersInformationRequest(url, select), true, false);
        }
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);
        final QueryCreator creator = new SQLQueryCreator();
        final String select = SQLConstants.SELECT + creator.getSelectCreateQuery(Article.class, SQLConstants.SQL_ARTICLE_SELECT) +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + " a" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + " f " + SQLConstants.ON + " a." + Article.ID + " = f." + Favorites.ART_ID +
                SQLConstants.WHERE + "(f." + Favorites.USER_ID + SQLConstants.EQ + searchParameter[1] + ") " +
                SQLConstants.AND + "(a." + Article.TYPE + SQLConstants.EQ + searchParameter[2] + ")" +
                SQLConstants.AND + "(f." + Favorites.ISFAVORITES + " = 1)" +
                SQLConstants.AND + "(a." + Article.SEARCH_NAME + SQLConstants.LIKE + "'%" + searchParameter[0].toLowerCase() + "%')" +
                SQLConstants.ORDER_BY + Article.NAME + SQLConstants.ASC;

        dao.get(new ParametersInformationRequest(null, select), false, true);
    }

}
