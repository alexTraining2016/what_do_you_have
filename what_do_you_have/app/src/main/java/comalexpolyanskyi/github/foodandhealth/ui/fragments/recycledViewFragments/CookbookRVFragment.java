package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.FavoritesFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.SimpleItemTouchHelperCallback;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class CookbookRVFragment extends BaseRVFragment implements ItemTouchHelperAdapter {

    private InteractionContract.Mediator<String> mediator;
    private static final String TYPE_ART = "all_type";

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
            this.mediator = new FavoritesFragmentMediator(this);
            mediator.loadData(getArguments().getString(AuthConstant.TOKEN), getArguments().getString(AuthConstant.ID), TYPE_ART);
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
        getAdapter().notifyItemRemoved(position);
        ArticleListItemDO itemDO = getAdapter().getItem(position);

        if(getAdapter().getItemCount() == 1){
            getAdapter().changeCursor(null);
        }

        mediator.loadData(getArguments().getString(AuthConstant.TOKEN), getArguments().getString(AuthConstant.ID), TYPE_ART, String.valueOf(itemDO.getId()));
    }
}
