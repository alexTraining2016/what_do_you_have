package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter.viewHolders.ItemViewHolder;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter.viewHolders.SectionViewHolder;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.SectionCursorAdapter;

public class IngredientSectionCursorAdapter extends SectionCursorAdapter<String, SectionViewHolder, ItemViewHolder> {

    private HashSet<Integer> selectedId;

    public IngredientSectionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0, R.layout.item_section, R.layout.ingredient_list_item);
        selectedId = new HashSet<>();

    }

    public void updateDataSet(Cursor cursor) {
        if (cursor != null) {
            changeCursor(cursor);
            notifyDataSetChanged();
            selectedId = new HashSet<>(getCursor().getCount());
        }
    }

    private boolean isSelected(Integer id) {
        return selectedId.contains(id);
    }

    @Override
    protected String getSectionFromCursor(Cursor cursor) {
        final int columnIndex = cursor.getColumnIndex(Ingredient.NAME);
        final String name = cursor.getString(columnIndex);

        return name.toUpperCase().substring(0, 1);
    }

    @Override
    protected SectionViewHolder createSectionViewHolder(View sectionView, String section) {
        return new SectionViewHolder(sectionView);
    }

    @Override
    protected void bindSectionViewHolder(int position, SectionViewHolder sectionViewHolder, ViewGroup parent, String section) {
        sectionViewHolder.textView.setText(section);
    }

    @Override
    protected ItemViewHolder createItemViewHolder(Cursor cursor, View itemView) {
        return new ItemViewHolder(itemView);
    }

    @Override
    protected void bindItemViewHolder(ItemViewHolder itemViewHolder, Cursor cursor, ViewGroup parent) {
        final Integer id = cursor.getInt(cursor.getColumnIndex(Ingredient.ID));
        itemViewHolder.rootView.setTag(R.string.app_name, id);
        itemViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(Ingredient.NAME)));
        App.getImageLoader().loadImageFromUrl(cursor.getString(cursor.getColumnIndex(Ingredient.IMAGE)), itemViewHolder.background);
        if(isSelected(id)){
            itemViewHolder.checkedView.setImageResource(R.drawable.ic_beenhere_green_60dp);
        }else{
            itemViewHolder.checkedView.setImageResource(R.drawable.ic_beenhere_white_60dp);
        }
        itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    public HashSet<Integer> getSelectedId() {
        return selectedId;
    }

    @Override
    protected int getMaxIndexerLength() {
        return 1;
    }
}