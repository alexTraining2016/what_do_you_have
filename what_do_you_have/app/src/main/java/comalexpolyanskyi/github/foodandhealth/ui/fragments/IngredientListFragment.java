package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.IngredientListFragmentPresenter;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragment extends Fragment implements IMVPContract.RequiredView<SparseArrayCompat<ListItemBean>> {

    private View progressBar;
    private View view;
    private RecyclerView recyclerView;
    private IMVPContract.Presenter<Void> presenter;

    public IngredientListFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        progressBar = view.findViewById(R.id.list_fragment_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment);
        startMVP(savedInstanceState);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startMVP(Bundle savedInstanceState) {
        if (savedInstanceState == null ) {
            this.presenter = new IngredientListFragmentPresenter(this);
            presenter.loadData(null);
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public void returnData(SparseArrayCompat<ListItemBean> response) {

    }

    @Override
    public void returnError(String message) {

    }

    @Override
    public void showProgress(boolean isInProgress) {

    }
}
