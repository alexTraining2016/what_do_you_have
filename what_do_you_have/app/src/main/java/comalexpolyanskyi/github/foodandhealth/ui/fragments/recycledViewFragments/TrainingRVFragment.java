package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.database.Cursor;
import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.presenter.TrainingListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class TrainingRVFragment extends BaseRVFragment {

    private MVPContract.Presenter<String, Cursor> presenter;

    public TrainingRVFragment() {
    }

    public static TrainingRVFragment newInstance(Bundle bundle) {
        TrainingRVFragment fragment = new TrainingRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new TrainingListFragmentPresenter(this);
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
}
