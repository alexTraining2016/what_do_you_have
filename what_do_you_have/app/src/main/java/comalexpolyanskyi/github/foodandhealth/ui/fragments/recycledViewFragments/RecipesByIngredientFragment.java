package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.os.Bundle;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.RecipesByIngFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class RecipesByIngredientFragment extends BaseRVFragment {

    private InteractionContract.Mediator<String> mediator;
    public static final String INGREDIENT_ID_SET = "ingredientIdSet";

    public static RecipesByIngredientFragment newInstance(Bundle args) {
        RecipesByIngredientFragment fragment = new RecipesByIngredientFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new RecipesByIngFragmentMediator(this);
            final String setOfPicking = pickingSet();
            mediator.loadData(setOfPicking, getArguments().getString(AuthConstant.TOKEN));
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mediator != null) {
            final String setOfPicking = pickingSet();
            mediator.search(setOfPicking, newText);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private String pickingSet() {
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
