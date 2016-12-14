package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.DietsRecipesListFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class DietRecipesRVFragment extends BaseRVFragment {
    private InteractionContract.Mediator<String> mediator;

    public DietRecipesRVFragment() {
        super();
    }

    public static DietRecipesRVFragment newInstance(Bundle bundle) {
        DietRecipesRVFragment fragment = new DietRecipesRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new DietsRecipesListFragmentMediator(this);
            mediator.loadData(getArguments().getString(AuthConstant.TOKEN));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mediator != null) {
            mediator.search(newText);

            return true;
        } else {
            return false;
        }
    }
}
