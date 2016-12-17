package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;


import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.FavoritesFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;

public class FavoritesFragmentMediator extends BaseMediator<Cursor, String> {

    private static final String SHORT_ART_T_NAME = " a";
    private static final String COMMA = ", ";
    private static final String SATN_PLUS_DOT = SHORT_ART_T_NAME + ".";
    private static final String SATN_PLUS_C_DOT = COMMA + SATN_PLUS_DOT;



    public FavoritesFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);
        this.dao = new FavoritesFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        final String select = SQLConstants.SELECT + SATN_PLUS_DOT + Article.ID + SATN_PLUS_C_DOT + Article.NAME + SATN_PLUS_C_DOT + Article.IMAGE_URI + SATN_PLUS_C_DOT + Article.TYPE +
                SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + " f " + SQLConstants.ON + " a." + Article.ID + " = f." + Favorites.ART_ID +
                SQLConstants.WHERE + "(f." + Favorites.USER_ID + " = " + parameters[1] + ") " +
                SQLConstants.AND + "(a." + Article.TYPE + SQLConstants.IN + "(" + parameters[2] + ")) " +
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
    }


}
