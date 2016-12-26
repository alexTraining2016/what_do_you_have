package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Locale;

import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.sql.QueryCreator;
import comalexpolyanskyi.github.foodandhealth.sql.SQLConstants;
import comalexpolyanskyi.github.foodandhealth.sql.SQLQueryCreator;

public class DescriptionActivityMediator extends BaseMediator<ArticleDO, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;
    private static final int TOKEN_KEY = 2;
    private static final int ACTION_KEY = 3;
    private static final int ARTICLE_ID_KEY = 1;
    public static final String LOAD = "load";
    public static final String UPDATE = "update";

    public DescriptionActivityMediator(@NonNull InteractionContract.RequiredView<ArticleDO> view) {
        super(view);

        dao = new DescriptionDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        final QueryCreator creator = new SQLQueryCreator();
        final String selectParam = SQLConstants.SELECT + creator.getSelectCreateQuery(ArticleDescription.class, SQLConstants.SQL_ARTICLE_SELECT) +
                ", " + creator.getSelectCreateQuery(Favorites.class, SQLConstants.SQL_FAVORITES_SELECT);
        final String whereParam = SQLConstants.WHERE + "a." + ArticleDescription.ID + SQLConstants.EQ + parameters[ARTICLE_ID_KEY];
        final String fromParam = SQLConstants.FROM + DBHelper.getTableName(ArticleDescription.class) + " a" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + " f" +
                SQLConstants.ON + "a." + Article.ID + SQLConstants.EQ + "f." + Favorites.ART_ID;

        final String query = selectParam + fromParam + whereParam;
        if (parameters[0].equals(LOAD)) {
            super.loadData(parameters);
            final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_DESC + parameters[ARTICLE_ID_KEY] + ApiConstants.API_BY_AUTH + parameters[TOKEN_KEY];

            dao.get(new ParametersInformationRequest(url, query), true, false);

        } else if (parameters[0].equals(UPDATE)) {
            final String url = ApiConstants.API_BASE_URL +
                    ApiConstants.API_ARTICLES_DESC + parameters[ARTICLE_ID_KEY] +
                    ApiConstants.API_BY_AUTH + parameters[TOKEN_KEY] +
                    ApiConstants.API_ACT + parameters[ACTION_KEY];

            dao.update(new ParametersInformationRequest(url, query));
        }
    }
}
