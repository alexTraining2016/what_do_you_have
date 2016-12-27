package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleListItemDO;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.FavoritesFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ItemTouchHelperAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.SimpleItemTouchHelperCallback;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class FitnessFavoritesRVFragment extends BaseRVFragment implements ItemTouchHelperAdapter {

    private InteractionContract.Mediator<String> mediator;
    public static final String TYPE_ART = "2";

    public FitnessFavoritesRVFragment() {
    }

    public static FitnessFavoritesRVFragment newInstance(Bundle bundle) {
        final FitnessFavoritesRVFragment fragment = new FitnessFavoritesRVFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected RecyclerView bindRecyclerView() {
        final RecyclerView recyclerView = super.bindRecyclerView();

        final ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return recyclerView;
    }

    @Override
    public void bindPresenter(Bundle savedInstanceState) {
        this.mediator = new FavoritesFragmentMediator(this);
        if (savedInstanceState == null) {
            final String token = getArguments().getString(AuthConstant.TOKEN);
            if (mediator.accessCheck(token)) {
                mediator.loadData(token, getArguments().getString(AuthConstant.ID), TYPE_ART);
            } else {
                super.returnError(getString(R.string.access_error));
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
        mediator = null;
    }

    @Override
    public void returnError(String message) {
        int count = getAdapter().getItemCount();
        if (count > 0) {
            super.returnError(message);
        } else {
            super.returnError(ContextHolder.getContext().getString(R.string.empty_favorites));
            super.data = null;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mediator != null) {
            mediator.search(newText, getArguments().getString(AuthConstant.ID), TYPE_ART);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemDismiss(int position) {
        getAdapter().notifyItemRemoved(position);
        final ArticleListItemDO itemDO = getAdapter().getItem(position);

        if (getAdapter().getItemCount() == 1) {
            getAdapter().changeCursor(null);
        }

        mediator.loadData(getArguments().getString(AuthConstant.TOKEN), getArguments().getString(AuthConstant.ID), TYPE_ART, String.valueOf(itemDO.getId()));
    }
}
