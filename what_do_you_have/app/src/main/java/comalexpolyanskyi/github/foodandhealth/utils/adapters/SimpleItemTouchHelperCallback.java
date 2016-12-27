package comalexpolyanskyi.github.foodandhealth.utils.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    public SimpleItemTouchHelperCallback(final ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

}