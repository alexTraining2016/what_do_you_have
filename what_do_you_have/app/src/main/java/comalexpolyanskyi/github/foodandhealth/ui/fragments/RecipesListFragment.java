package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.RecipesModel;
import comalexpolyanskyi.github.foodandhealth.models.pojo.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.models.pojo.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.ListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.utils.RecipesRVAdapter;

public class RecipesListFragment extends Fragment implements IMVPContract.RequiredView<SparseArrayCompat<ListItemBean>> {

    public static final String ACTION = "Action";
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String FRAGMENT_REQUEST_PARAMS_KEY = "params";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private View progressBar;
    private RecyclerView recyclerView;
    private View view;
    private IMVPContract.Presenter<QueryParameters> presenter;

    public RecipesListFragment() {
    }

    public static RecipesListFragment newInstance(QueryParameters parameters){
        RecipesListFragment fragment = new RecipesListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FRAGMENT_REQUEST_PARAMS_KEY, parameters);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        progressBar = view.findViewById(R.id.list_fragment_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment);
        startMVP(savedInstanceState);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView(){
        checkOrientation();
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), mColumnCount));
        recyclerView.setAdapter(new RecipesRVAdapter(RecipesModel.ITEMS, mListener));
    }

    public void startMVP(Bundle savedInstanceState) {
        if (savedInstanceState == null ) {
            this.presenter = new ListFragmentPresenter(this);
            QueryParameters parameters = (QueryParameters) getArguments().getSerializable(FRAGMENT_REQUEST_PARAMS_KEY);
            presenter.loadData(parameters);
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDestroy();
        mListener = null;
    }

    @Override
    public void returnData(SparseArrayCompat<ListItemBean> resultDataArray) {

    }

    @Override
    public void returnError(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if(isInProgress){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnListFragmentInteractionListener {
        void onRecipesFragmentInteraction(View v);
    }

    private void checkOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mColumnCount = 2;
        }else {
            mColumnCount = 3;
        }
    }
}