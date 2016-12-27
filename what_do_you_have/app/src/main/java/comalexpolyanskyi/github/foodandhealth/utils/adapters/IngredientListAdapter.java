package comalexpolyanskyi.github.foodandhealth.utils.adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.abstractAdapter.AbstractAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;

public class IngredientListAdapter extends AbstractAdapter<IngredientItemDO> {

    private Cursor cursor;
    private MySimpleImageLoader imageLoader;
    private HashSet<Integer> selectedId;
    private View.OnClickListener clickListener;

    public IngredientListAdapter(@NonNull final Cursor items) {
        cursor = items;
        imageLoader = App.getImageLoader();
        selectedId = new HashSet<>();
        clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer id = (Integer) v.getTag(R.string.app_name);

                if (isSelected(id)) {
                    selectedId.remove(id);
                    ((AppCompatImageView) v.findViewById(R.id.checked_image)).setImageResource(R.drawable.ic_beenhere_white_60dp);
                } else {
                    selectedId.add(id);
                    ((AppCompatImageView) v.findViewById(R.id.checked_image)).setImageResource(R.drawable.ic_beenhere_green_60dp);
                }
            }
        };
    }

    @Override
    public void onBind(final AbstractAdapter.AbstractViewHolder holder, final IngredientItemDO data, int position, int viewType) {
        holder.<TextView>get(R.id.ingredient_name).setText(data.getName());
        imageLoader.loadImageFromUrl(data.getImage(), holder.<ImageView>get(R.id.background_image), null);
        holder.<View>get(R.id.card_view).setOnClickListener(clickListener);
        holder.<View>get(R.id.card_view).setTag(R.string.app_name, data.getId());

        if (isSelected(data.getId())) {
            holder.<ImageView>get(R.id.checked_image).setImageResource(R.drawable.ic_beenhere_green_60dp);
        } else {
            holder.<ImageView>get(R.id.checked_image).setImageResource(R.drawable.ic_beenhere_white_60dp);
        }
    }

    public void changeCursor(final Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public IngredientItemDO getItem(int position) {
        cursor.moveToPosition(position);

        return new IngredientItemDO(cursor);
    }

    @Override
    public AbstractAdapter.AbstractViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new AbstractAdapter.AbstractViewHolder(LayoutInflater.from(ContextHolder.getContext()).inflate(R.layout.ingredient_list_item, parent, false),
                R.id.background_image, R.id.ingredient_name, R.id.checked_image, R.id.card_view);
    }

    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    private boolean isSelected(final Integer id) {
        return selectedId.contains(id);
    }

    public HashSet<Integer> getSelectedId() {
        return selectedId;
    }
}
