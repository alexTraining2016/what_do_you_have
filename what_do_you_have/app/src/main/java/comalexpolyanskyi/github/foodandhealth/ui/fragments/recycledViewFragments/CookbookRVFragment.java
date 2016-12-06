package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import comalexpolyanskyi.github.foodandhealth.mediators.BaseMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.SimpleItemTouchHelperCallback;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class CookbookRVFragment extends BaseRVFragment implements ItemTouchHelperAdapter {

    private InteractionContract.Mediator<String> mediator;

    public CookbookRVFragment() {
        super();
    }

    public static CookbookRVFragment newInstance(Bundle bundle) {
        CookbookRVFragment fragment = new CookbookRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected RecyclerView bindRecyclerView() {
        RecyclerView recyclerView = super.bindRecyclerView();

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return recyclerView;
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
