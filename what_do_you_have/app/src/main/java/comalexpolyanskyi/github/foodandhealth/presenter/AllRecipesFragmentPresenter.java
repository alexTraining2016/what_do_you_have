package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.ArticleListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
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
    public void loadData(String parameters) {
        super.loadData(parameters);
        String select = "SELECT * FROM " + DBHelper.getTableName(Article.class);
        String url = Api.API_BASE_URL+ Api.API_ARTICLES_All;
        String whereParam = " WHERE " + Article.TYPE + " BETWEEN " + ALL_FOOD_RECIPES + " AND " + ALL_DIET_RECIPES;
        String selectSql = select + whereParam;
        dao.get(new ParametersInformationRequest(url, selectSql, null), false);
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
    public void search(String searchParameter) {
        super.search(searchParameter);
        String selectFrom = "SELECT * FROM ";
        String where = " WHERE " + Article.TYPE + " BETWEEN " + ALL_FOOD_RECIPES
                + " AND " + ALL_DIET_RECIPES
                + " AND " + Article.NAME
                + " LIKE '%" + searchParameter.toLowerCase() + "%'";
        String select = selectFrom + DBHelper.getTableName(Article.class) + where;
        dao.get(new ParametersInformationRequest(null, select, null), true);
    }
}