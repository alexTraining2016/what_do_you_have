package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.models.dataObjects.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.presenter.ArticleListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.ArticlesTypeRequest;
import comalexpolyanskyi.github.foodandhealth.presenter.IMVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ArticleListFragmentAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.SimpleItemTouchHelperCallback;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;

public class ArticleListFragment extends Fragment implements IMVPContract.RequiredView<List<ArticleListItemDO>>, ItemTouchHelperAdapter {

    private static final String ACTION = "Action";
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String FRAGMENT_REQUEST_PARAMS_KEY = "params";
    private int columnCount = 2;
    private OnListFragmentInteractionListener listener;
    private View progressBar;
    private RecyclerView recyclerView;
    private View view;
    private List<ArticleListItemDO> data = new ArrayList<>();
    private IMVPContract.Presenter<QueryParameters> presenter;
    private ArticleListFragmentAdapter adapter;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(QueryParameters parameters){
        ArticleListFragment fragment = new ArticleListFragment();
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
            columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_article_list, container, false);
        view.findViewById(R.id.backgroundImage).setBackgroundResource(AppStyleHolder.initialize().getBgDrawable());
        progressBar = view.findViewById(R.id.list_fragment_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment);
        bindMVP(savedInstanceState);
        bindRecyclerView();
        return view;
    }

    private void bindRecyclerView(){
        checkOrientation();
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), columnCount));
        adapter = new ArticleListFragmentAdapter(data, listener);
        recyclerView.setAdapter(adapter);
        QueryParameters parameters = (QueryParameters) getArguments().getSerializable(FRAGMENT_REQUEST_PARAMS_KEY);
        if (parameters != null && (parameters.getViewType() == ArticlesTypeRequest.FAVORITES_FOOD_RECIPES
                || parameters.getViewType() == ArticlesTypeRequest.FAVORITES_TRAINING_AND_DIET_RECIPES)) {
            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(this);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    }

    public void bindMVP(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new ArticleListFragmentPresenter(this);
            QueryParameters parameters = (QueryParameters) getArguments().getSerializable(FRAGMENT_REQUEST_PARAMS_KEY);
            presenter.loadData(parameters);
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDestroy();
        listener = null;
    }

    @Override
    public void returnData(List<ArticleListItemDO> response) {
        data.addAll(response);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public interface OnListFragmentInteractionListener {
        void onRecipesFragmentInteraction(View v);
    }

    private void checkOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnCount = 2;
        }else {
            columnCount = 3;
        }
    }
}