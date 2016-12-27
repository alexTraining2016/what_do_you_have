package comalexpolyanskyi.github.foodandhealth.utils.adapters.abstractAdapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AbstractAdapter<Item> extends RecyclerView.Adapter<AbstractAdapter.AbstractViewHolder> {

    public abstract void onBind(final AbstractViewHolder holder, final Item item, int position, int viewType);

    public abstract Item getItem(int position);

    @Override
    public void onBindViewHolder(final AbstractViewHolder holder, final int position) {
        onBind(holder, getItem(position), position, getItemViewType(position));
    }

    public static class AbstractViewHolder extends RecyclerView.ViewHolder {

        final private SparseArrayCompat<View> viewSparseArray;

        public AbstractViewHolder(final View itemView, final int... ids) {
            super(itemView);

            viewSparseArray = new SparseArrayCompat<>(ids.length);

            for (final int id : ids) {
                viewSparseArray.append(id, itemView.findViewById(id));
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T get(final int id) {
            return (T) viewSparseArray.get(id);
        }

    }

}
