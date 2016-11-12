package comalexpolyanskyi.github.foodandhealth.presenter;

import android.database.Cursor;
import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.dao.IngredientListFragmentDAO;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Article;


public class TrainingListFragmentPresenter extends BasePresenter<Cursor, String>{

    private MVPContract.DAO<ParametersInformationRequest> dao;
    private static final String TRAINING = "2";

    public TrainingListFragmentPresenter(@NonNull MVPContract.RequiredView<Cursor> view) {
        super(view);
        this.dao = new IngredientListFragmentDAO(this);
    }

    @Override
    public void loadData(String parameters) {
        super.loadData(parameters);
        String url = Api.API_BASE_URL+ Api.API_ARTICLES + TRAINING;
        String selectSql = "SELECT * FROM " + DBHelper.getTableName(Article.class) + " WHERE " + Article.TYPE + " = " + TRAINING;
        dao.get(new ParametersInformationRequest(url, selectSql, null), false);
    }

    @Override
    public void search(String searchParameter) {
        super.search(searchParameter);
        String selectFrom = "SELECT * FROM ";
        String where = " WHERE " + Article.TYPE + " = " + TRAINING + " AND " + Article.NAME + " LIKE '%" + searchParameter.toLowerCase() + "%'";
        String selectSql = selectFrom + DBHelper.getTableName(Article.class) + where;
        dao.get(new ParametersInformationRequest(null, selectSql, null), true);
    }
}
