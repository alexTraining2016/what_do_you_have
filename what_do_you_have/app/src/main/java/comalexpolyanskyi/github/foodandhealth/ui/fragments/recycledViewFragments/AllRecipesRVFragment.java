package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.presenter.AllRecipesFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;

public class AllRecipesRVFragment extends BaseRVFragment {

    private MVPContract.Presenter<String> presenter;

    public AllRecipesRVFragment(){
        super();
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new AllRecipesFragmentPresenter(this);
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
