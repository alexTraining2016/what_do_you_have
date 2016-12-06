package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.mediators.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class FitnessFavoritesRVFragment extends BaseRVFragment implements ItemTouchHelperAdapter {

    private InteractionContract.Mediator<String> mediator;

    public FitnessFavoritesRVFragment() {
    }

    public static FitnessFavoritesRVFragment newInstance(Bundle bundle) {
        FitnessFavoritesRVFragment fragment = new FitnessFavoritesRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || mediator == null) {
            this.mediator = new BaseMediator(this);
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

    @Override
    public void onItemDismiss(int position) {
        // data.remove(position);
        // data.
        //  adapter.notifyItemRemoved(position);
    }
}
