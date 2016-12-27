package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.RecipesByKindFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;

public class RecipesByKindRVFragment extends BaseRVFragment {

    private InteractionContract.Mediator<String> mediator;
    private final static String KIND_KEY = "kid";

    public RecipesByKindRVFragment() {
        super();
    }

    public static RecipesByKindRVFragment newInstance(int kindId) {
        final RecipesByKindRVFragment fragment = new RecipesByKindRVFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KIND_KEY, kindId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        this.mediator = new RecipesByKindFragmentMediator(this);
        if (savedInstanceState == null) {
            mediator.loadData(getArguments().getInt(KIND_KEY) + "");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mediator != null) {
            mediator.onDestroy();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mediator != null) {
            mediator.search(newText, getArguments().getInt(KIND_KEY) + "");
            return true;
        } else {
            return false;
        }
    }
}
