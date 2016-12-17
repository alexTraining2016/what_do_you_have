package comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.DescriptionDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.mediators.ApiConstants;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.baseMediator.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.utils.commonConstants.SQLConstants;


public class DescriptionActivityMediator extends BaseMediator<ArticleDO, String> {

    private InteractionContract.DAO<ParametersInformationRequest> dao;
    public static final String LOAD = "load";
    public static final String UPDATE = "update";

    public DescriptionActivityMediator(@NonNull InteractionContract.RequiredView<ArticleDO> view) {
        super(view);

        dao = new DescriptionDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        final String whereParam = SQLConstants.WHERE + "a." + ArticleDescription.ID + " = " + parameters[1];
        final String select = SQLConstants.SELECT + "a." + ArticleDescription.ID + ", a." + ArticleDescription.NAME + ", a." + ArticleDescription.DESCRIPTION +
                ", a." + ArticleDescription.LIKE_COUNT + ", a." + ArticleDescription.REPOST_COUNT + ", a." + ArticleDescription.AGING_TIME + ", a." + ArticleDescription.RECORDING_TIME +
                ", a." + ArticleDescription.IMAGE_URI + ", f." + Favorites.ISLIKE + ", f." + Favorites.ISFAVORITES + ", f." + Favorites.USER_ID +
                SQLConstants.FROM + DBHelper.getTableName(ArticleDescription.class) + " a" +
                SQLConstants.INNER_JOIN + DBHelper.getTableName(Favorites.class) + " f" +
                SQLConstants.ON + "a." + Article.ID + " = " + "f." + Favorites.ART_ID
                + whereParam;

        if (parameters[0].equals(LOAD)) {
            super.loadData(parameters);
            final String url = ApiConstants.API_BASE_URL + ApiConstants.API_ARTICLES_DESC + parameters[1] + ApiConstants.API_BY_AUTH + parameters[2];

            dao.get(new ParametersInformationRequest(url, select), true, false);
        } else if (parameters[0].equals(UPDATE)) {
            final String url = ApiConstants.API_BASE_URL +
                    ApiConstants.API_ARTICLES_DESC + parameters[1] +
                    ApiConstants.API_BY_AUTH + parameters[2] +
                    ApiConstants.API_ACT + parameters[3];

            dao.update(new ParametersInformationRequest(url, select));
        }
    }
}
