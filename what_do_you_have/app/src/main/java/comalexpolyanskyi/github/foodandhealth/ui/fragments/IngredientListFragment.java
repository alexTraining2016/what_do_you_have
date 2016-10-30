package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.pojo.IngredientItemModel;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.IngredientListFragmentPresenter;

/**
 * Created by Алексей on 22.10.2016.
 */

public class IngredientListFragment extends Fragment implements IMVPContract.RequiredView<List<IngredientItemModel>> {

    private View progressBar;
    private static final String ACTION = "Action";
    private View view;
    private ListView listView;
    private ListAdapter arrayAdapter;
    private IMVPContract.Presenter<Void> presenter;
    private List<IngredientItemModel> data = new ArrayList<>();

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
        arrayAdapter = new ArrayAdapter<IngredientItemModel>(getContext(), R.layout.ingredient_list_item, data){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_list_item, parent, false);
                } else {
                    view = convertView;
                }
                bindView(position, view);
                return view;
            }

            private void bindView(int position, View view) {
                IngredientItemModel ingredient = getItem(position);
                ((TextView) view.findViewById(R.id.ingredient_name)).setText(ingredient.getName());
                view.findViewById(R.id.ingredient_name).setTag(ingredient.getId());
            }
        };
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
    public void returnData(List<IngredientItemModel> response) {
        data.clear();
        data.addAll(response);
        Log.i("123", "12345");
        ((ArrayAdapter)arrayAdapter).notifyDataSetChanged();

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
