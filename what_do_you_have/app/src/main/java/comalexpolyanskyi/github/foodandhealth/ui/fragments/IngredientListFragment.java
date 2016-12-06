package comalexpolyanskyi.github.foodandhealth.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.mediators.IngredientListFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter.IngredientSectionCursorAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;


public class IngredientListFragment extends Fragment implements InteractionContract.RequiredView<Cursor> {

    private static final String ACTION = "Action";
    private View progressBar;
    private View view;
    private ListView listView;
    private IngredientSectionCursorAdapter arrayAdapter;
    private InteractionContract.Mediator<String> mediator;
    private Cursor data;
    private OnFABClickListener mainActivityCallback;


    public interface OnFABClickListener {
        void onRecipesByIngredient(HashSet<Integer> ingredientsSet);
    }

    public static IngredientListFragment newInstance(Bundle bundle){
        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public IngredientListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        progressBar = view.findViewById(R.id.list_fragment_progress);
        view.findViewById(R.id.list_fragment_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashSet<Integer> ingredientSet =  arrayAdapter.getSelectedId();
                if(ingredientSet.size() != 0) {
                    mainActivityCallback.onRecipesByIngredient(ingredientSet);
                }else{
                    Snackbar.make(view, getContext().getString(R.string.no_ing_mess), Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
                }

            }
        });

        listView = (ListView) view.findViewById(R.id.ingredient_list_view);
        bindListView();
        bindMVP(savedInstanceState);

        return view;
    }

    private void bindListView() {
        arrayAdapter = new IngredientSectionCursorAdapter(getContext(), data);
        listView.setFastScrollEnabled(true);
        listView.setFastScrollAlwaysVisible(true);
        listView.setAdapter(arrayAdapter);
    }

    public void bindMVP(Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new IngredientListFragmentMediator(this);
            mediator.loadData(getArguments().getString(AuthConstant.TOKEN));
        }
    }

    @Override
    public void returnData(Cursor response) {
        data = response;
        arrayAdapter.updateDataSet(response);
    }

    @Override
    public void returnError(String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if (isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
