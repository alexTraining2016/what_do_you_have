package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.SectionCursorAdapter;

public class IngredientSectionCursorAdapter extends SectionCursorAdapter <String, SectionViewHolder, ItemViewHolder> {

    private Context context;
    private Set<Integer> selectedId = new HashSet<>();

    public IngredientSectionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0, R.layout.item_section, R.layout.ingredient_list_item);
        this.context = context;
    }

    public void updateDataSet(Cursor cursor)
    {
        if(cursor != null) {
            changeCursor(cursor);
            notifyDataSetChanged();
            selectedId = new HashSet<>(getCursor().getCount());
        }
    }

    private Set<Integer> getSelectedId(){
        return selectedId;
    }

    private boolean isSelected(Integer id){
        return selectedId.contains(id);
    }

    @Override
    protected String getSectionFromCursor(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(Ingredient.NAME);
        String name = cursor.getString(columnIndex);
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
        Integer id = cursor.getInt(cursor.getColumnIndex(Ingredient.ID));
        itemViewHolder.rootView.setTag(R.string.app_name, id);
        itemViewHolder.name.setText(cursor.getString(cursor.getColumnIndex(Ingredient.NAME)));
        bindBackground(itemViewHolder.name, itemViewHolder.rootView, isSelected(id));
        itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = (Integer) v.getTag(R.string.app_name);
                ObjectAnimator animAlpha = ObjectAnimator.ofFloat(v, "alpha", 0.1f, 0.9f);
                animAlpha.setDuration(450);
                animAlpha.start();
                boolean isSelected = bindBackground((TextView) v.findViewById(R.id.ingredient_name), v, !isSelected(id));
                if(isSelected){
                    selectedId.remove(id);
                }else{
                    selectedId.add(id);
                }
            }
        });
    }


    private boolean bindBackground(TextView textView, View view, boolean isSelected){
        if(isSelected) {
            view.setBackground(context.getResources().getDrawable(R.drawable.pressed));
            textView.setTextColor(Color.WHITE);
            return false;
        }else{
            view.setBackground(context.getResources().getDrawable(R.drawable.normal));
            textView.setTextColor(Color.BLACK);
            return true;
        }
    }

    @Override
    protected int getMaxIndexerLength() {
        return 1;
    }
}