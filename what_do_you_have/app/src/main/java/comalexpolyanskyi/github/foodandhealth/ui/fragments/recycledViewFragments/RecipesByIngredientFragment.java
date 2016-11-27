package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.database.Cursor;
import android.os.Bundle;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.RecipesByIngFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class RecipesByIngredientFragment extends BaseRVFragment {

    private MVPContract.Presenter<String, Cursor> presenter;
    public static final String INGREDIENT_ID_SET = "ingredientIdSet";

    public static RecipesByIngredientFragment newInstance(Bundle args) {
        RecipesByIngredientFragment fragment = new RecipesByIngredientFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new RecipesByIngFragmentPresenter(this);
            final String setOfPicking = pickingSet();
            presenter.loadData(setOfPicking, getArguments().getString(AuthConstant.TOKEN));
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(presenter != null){
            final String setOfPicking = pickingSet();
            presenter.search(setOfPicking, newText);
            return true;
        }else{
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private String pickingSet(){
        final HashSet<Integer> ingredientsIdSet = (HashSet<Integer>) getArguments().getSerializable(INGREDIENT_ID_SET);
        String params = "";
        if (ingredientsIdSet != null) {
            Integer[] array = ingredientsIdSet.toArray(new Integer[ingredientsIdSet.size()]);

            for (int i = 0; i < ingredientsIdSet.size(); i++) {
                if (ingredientsIdSet.size() - 1 > i) {
                    params += array[i].toString() + ",";
                } else {
                    params += array[i].toString();
                }
            }
        }

        return params;
    }
}
