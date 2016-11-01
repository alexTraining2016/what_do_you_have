package comalexpolyanskyi.github.foodandhealth.models;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.models.dataObjects.ListItemDO;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragmentModel implements IMVPContract.Model {

    private IMVPContract.RequiredPresenter<SparseArrayCompat<ListItemDO>> presenter;
    private SparseArrayCompat<ListItemDO> sparseArrays = null;

    public IngredientListFragmentModel(@NonNull IMVPContract.RequiredPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getData(String url) {

    }

    @Override
    public void onDestroy() {

    }
}
