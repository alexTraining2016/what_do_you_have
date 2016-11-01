package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.IngredientListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.IngredientListAdapter;


public class IngredientListFragment extends Fragment implements IMVPContract.RequiredView<List<IngredientItemDO>> {

    private View progressBar;
    private static final String ACTION = "Action";
    private View view;
    private ListView listView;
    private IngredientListAdapter arrayAdapter;
    private IMVPContract.Presenter<Void> presenter;
    private List<IngredientItemDO> data = new ArrayList<>();

    public IngredientListFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        progressBar = view.findViewById(R.id.list_fragment_progress);
        listView = (ListView) view.findViewById(R.id.ingredient_list_view);
        setRetainInstance(true);
        bindListView();
        bindMVP(savedInstanceState);
        return view;
    }

    private void bindListView(){
        arrayAdapter = new IngredientListAdapter(getContext(), data, R.layout.ingredient_list_item);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void bindMVP(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new IngredientListFragmentPresenter(this);
            presenter.loadData(null);
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public void returnData(List<IngredientItemDO> response) {
        data.clear();
        data.addAll(response);
        arrayAdapter.updateDataSet();

    }

    @Override
    public void returnError(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if(isInProgress){
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

}
