package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;

public class AllRecipesFragmentPresenter extends BasePresenter<Cursor, String> {

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String ALL_DIET_RECIPES = "1";
    private static final String ALL_FOOD_RECIPES = "0";

    public AllRecipesFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);

        this.dao = new ArticleListFragmentDAO(this);
    }

    @Override
    public void loadData(String... parameters) {
        super.loadData(parameters);

        final String url = Api.API_BASE_URL + Api.API_ARTICLES_All + Api.API_BY_AUTH + parameters[0];
        final String whereParam = SQL.WHERE + Article.TYPE
                + SQL.BETWEEN + ALL_FOOD_RECIPES + SQL.AND + ALL_DIET_RECIPES;
        final String selectSql = SQL.S_F + DBHelper.getTableName(Article.class) + whereParam;

        dao.get(new ParametersInformationRequest(url, selectSql, null), false, false);
    }

   /* @Override
            case ArticlesTypeRequest.FOOD_RECIPES_BY_INGREDIENT:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.FAVORITES_FOOD_RECIPES:
                url = REQUEST_URL;
                break;
            case ArticlesTypeRequest.FAVORITES_TRAINING_AND_DIET_RECIPES:
                url = REQUEST_URL;
                break;
        }
    }*/

    @Override
    public void search(String... searchParameter) {
        super.search(searchParameter);

        final String where = SQL.WHERE + Article.TYPE + SQL.BETWEEN + ALL_FOOD_RECIPES
                + SQL.AND + ALL_DIET_RECIPES
                + SQL.AND + Article.SEARCH_NAME
                + SQL.LIKE + "'%" + searchParameter[0].toLowerCase() + "%'";
        final String select = SQL.S_F + DBHelper.getTableName(Article.class) + where;

        dao.get(new ParametersInformationRequest(null, select, null), true, false);
    }
}