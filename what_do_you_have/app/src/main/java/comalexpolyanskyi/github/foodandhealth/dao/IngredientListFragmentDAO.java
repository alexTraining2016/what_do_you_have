package comalexpolyanskyi.github.foodandhealth.dao;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ListItemDO;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;

public class IngredientListFragmentDAO implements IMVPContract.DAO<String> {

    private IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemDO>> presenter;
    private SparseArrayCompat<ListItemDO> sparseArrays = null;

    public IngredientListFragmentDAO(@NonNull IMVPContract.RequiredPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void get(String url) {

    }

    @Override
    public void delete(String parameters) {

    }

    @Override
    public void post(String parameters) {

    }

    @Override
    public void put(String parameters) {

    }
}
