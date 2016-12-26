package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ArticleListFragmentAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;

abstract public class BaseRVFragment extends Fragment implements InteractionContract.RequiredView<Cursor>, SearchView.OnQueryTextListener {

    private static final String ACTION = "Action";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int columnCount = 1;
    private OnListFragmentInteractionListener listener;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View view;
    protected Cursor data;
    private ArticleListFragmentAdapter adapter;

    public interface OnListFragmentInteractionListener {
        void onRecipesFragmentInteraction(View v);
    }

    public BaseRVFragment() {
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
        setHasOptionsMenu(true);
        progressBar = (ProgressBar) view.findViewById(R.id.list_fragment_progress);
        progressBar.getIndeterminateDrawable().setColorFilter(AppStyleHolder.initialize().getColor(), PorterDuff.Mode.MULTIPLY);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fragment);
        bindPresenter(savedInstanceState);
        bindRecyclerView();

        return view;
    }

    protected ArticleListFragmentAdapter getAdapter(){
        return adapter;
    }

    protected RecyclerView bindRecyclerView() {
        checkOrientation();
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), columnCount));
        adapter = new ArticleListFragmentAdapter(data, listener);
        recyclerView.setAdapter(adapter);

        return recyclerView;
    }

    abstract public void bindPresenter(Bundle savedInstanceState);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    abstract public boolean onQueryTextChange(String newText);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    @Override
    public void returnData(Cursor response) {
        this.data = response;
        adapter.changeCursor(response);
    }

    @Override
    public void returnError(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if (isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void checkOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnCount = 1;
        } else {
            columnCount = 2;
        }
    }
}