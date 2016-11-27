package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.database.Cursor;
import android.os.Bundle;

import comalexpolyanskyi.github.foodandhealth.presenter.AllRecipesFragmentPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class AllRecipesRVFragment extends BaseRVFragment {

    private MVPContract.Presenter<String, Cursor> presenter;

    public AllRecipesRVFragment(){
        super();
    }

    public static AllRecipesRVFragment newInstance(Bundle bundle){
        AllRecipesRVFragment fragment = new AllRecipesRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        if (savedInstanceState == null || presenter == null) {
            this.presenter = new AllRecipesFragmentPresenter(this);
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
        if(presenter != null){
            presenter.search(newText);
            return true;
        }else{
            return false;
        }
    }
}
