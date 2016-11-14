package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.database.Cursor;
import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.presenter.DietsRecipesListFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class DietRecipesRVFragment extends BaseRVFragment {
    private MVPContract.Presenter<String, Cursor> presenter;

    public DietRecipesRVFragment(){
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new DietsRecipesListFragmentPresenter(this);
            presenter.loadData(null);
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
        if(presenter != null){
            presenter.search(newText);
            return true;
        }else{
            return false;
        }
    }
}
