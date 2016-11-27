package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.database.Cursor;
import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.presenter.BasePresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class FitnessFavoritesRVFragment extends BaseRVFragment implements ItemTouchHelperAdapter {

    private MVPContract.Presenter<String, Cursor> presenter;

    public FitnessFavoritesRVFragment() {
    }

    public static FitnessFavoritesRVFragment newInstance(Bundle bundle) {
        FitnessFavoritesRVFragment fragment = new FitnessFavoritesRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new BasePresenter(this);
            presenter.loadData(getArguments().getString(AuthConstant.TOKEN));
        } else {
            this.presenter.onConfigurationChanged(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        presenter.onDestroy();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (presenter != null) {
            presenter.search(newText);

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
