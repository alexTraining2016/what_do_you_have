package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.IngredientListFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.IngredientListAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class IngredientListFragment extends Fragment implements InteractionContract.RequiredView<Cursor> {

    private static final String ACTION = "Action";
    private View progressBar;
    private View view;
    private InteractionContract.Mediator<String> mediator;
    private Cursor data;
    private IngredientListAdapter adapter;
    private RecyclerView recyclerView;
    private OnFABClickListener mainActivityCallback;

    public interface OnFABClickListener {

        void onRecipesByIngredient(HashSet<Integer> ingredientsSet);
    }

    public static IngredientListFragment newInstance(Bundle bundle) {
        final IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public IngredientListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mainActivityCallback = (OnFABClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        progressBar = view.findViewById(R.id.list_fragment_progress);
        view.findViewById(R.id.list_fragment_fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final HashSet<Integer> ingredientSet = adapter.getSelectedId();
                if (ingredientSet.size() != 0) {
                    mainActivityCallback.onRecipesByIngredient(ingredientSet);
                } else {
                    Snackbar.make(view, getContext().getString(R.string.no_ing_mess), Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
                }

            }
        });

        bindListView(savedInstanceState);
        bindMVP(savedInstanceState);

        return view;
    }

    private void bindListView(final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            adapter = new IngredientListAdapter(data);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void bindMVP(final Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new IngredientListFragmentMediator(this);
            mediator.loadData(getArguments().getString(AuthConstant.TOKEN));
        }
    }

    @Override
    public void returnData(final Cursor response) {
        data = response;
        adapter.changeCursor(data);
    }

    @Override
    public void returnError(final String message) {
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
}
