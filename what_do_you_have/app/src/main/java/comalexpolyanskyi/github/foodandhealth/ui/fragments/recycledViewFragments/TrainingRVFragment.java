package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.TrainingListFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class TrainingRVFragment extends BaseRVFragment {

    private InteractionContract.Mediator<String> mediator;

    public TrainingRVFragment() {
    }

    public static TrainingRVFragment newInstance(Bundle bundle) {
        final TrainingRVFragment fragment = new TrainingRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new TrainingListFragmentMediator(this);
            mediator.loadData(getArguments().getString(AuthConstant.TOKEN));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
        mediator = null;
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
