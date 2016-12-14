package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;


import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.dao.fragmentsDAO.FavoritesFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class FavoritesFragmentMediator extends BaseMediator<Cursor, String> {

    private static final String SHORT_ART_T_NAME = " a";
    private static final String SATN_PLUS_DOT = SHORT_ART_T_NAME + ".";
    private static final String SHORT_FAV_T_NAME = " f";
    private static final String SFTN_PLUS_DOT = SHORT_FAV_T_NAME + ".";
    private static final String ALL_DIET_RECIPES = "1";
    private static final String ALL_FOOD_RECIPES = "0";
    private static final String COMMA = ", ";
    private static final String EQUALLY = " = ";

    private InteractionContract.DAO<ParametersInformationRequest> dao;

    public FavoritesFragmentMediator(@NonNull InteractionContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new FavoritesFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {

        final String select = SQLConstants.SELECT + SATN_PLUS_DOT + Article.ID + COMMA + SATN_PLUS_DOT + Article.IMAGE_URI + COMMA+
                SATN_PLUS_DOT + Article.NAME + ", " + SATN_PLUS_DOT + Article.TYPE + COMMA + SATN_PLUS_DOT + Article.AGING_TIME + COMMA + SATN_PLUS_DOT + Article.RECORDING_TIME;
        final String sql = select + SQLConstants.FROM + DBHelper.getTableName(Article.class) + SHORT_ART_T_NAME +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + SHORT_FAV_T_NAME +
                SQLConstants.ON + SATN_PLUS_DOT + Article.ID + " = " + SFTN_PLUS_DOT + Favorites.ART_ID;
        final String where = SQLConstants.WHERE + SFTN_PLUS_DOT + Favorites.USER_ID + EQUALLY + parameters[1] +
                SQLConstants.AND + SATN_PLUS_DOT + Article.TYPE + SQLConstants.BETWEEN + ALL_FOOD_RECIPES + SQLConstants.AND + ALL_DIET_RECIPES +
                SQLConstants.AND + SFTN_PLUS_DOT + Favorites.ISFAVORITES + " = 1";
        final String url = ApiConstants.API_BASE_URL + ApiConstants.API_FOR_FAVORITES + parameters[2] + ApiConstants.API_BY_AUTH + parameters[0];

        if(parameters.length > 3){
            final ContentValues updateValues = new ContentValues();
            updateValues.put(Favorites.ISFAVORITES, 0);
            final String wArg = Favorites.ART_ID + EQUALLY + parameters[3];

            dao.get(new ParametersInformationRequest(url+ApiConstants.API_REMOVE_FROM_FAVORITES + parameters[3], sql + where, wArg, updateValues), true, false);
        }else {
            super.loadData(parameters);

            dao.get(new ParametersInformationRequest(url, sql + where), true, false);
        }
    }

    @Override
    public void onError() {
        super.noDataErrorMessage = ContextHolder.getContext().getString(R.string.empty_favorites);
        super.onError();
    }

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);
    }


}
