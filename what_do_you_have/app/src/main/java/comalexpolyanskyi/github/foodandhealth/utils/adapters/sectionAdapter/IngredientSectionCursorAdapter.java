package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.IngredientItemDO;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.SectionCursorAdapter;

public class IngredientSectionCursorAdapter extends SectionCursorAdapter <String, SectionViewHolder, ItemViewHolder> {

    private Context context;

    public IngredientSectionCursorAdapter(Context context, Cursor c) {
        super(context, c, 0, R.layout.item_section, R.layout.ingredient_list_item);
        this.context = context;
    }

    public void updateDataSet(Cursor cursor)
    {
        if(cursor != null) {
            changeCursor(cursor);
            notifyDataSetChanged();
        }
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
        final IngredientItemDO ingredient = new IngredientItemDO(cursor.getInt(cursor.getColumnIndex(Ingredient.ID)),
                cursor.getString(cursor.getColumnIndex(Ingredient.NAME)));
        itemViewHolder.name.setTag(ingredient.getId());
        itemViewHolder.name.setText(ingredient.getName());
        bindBackground(itemViewHolder.name, itemViewHolder.rootView, ingredient);
        itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                ObjectAnimator animAlpha = ObjectAnimator.ofFloat(v, "alpha", 0.1f, 0.9f);
                animAlpha.setDuration(450);
                animAlpha.start();
                boolean isSelect = bindBackground((TextView) v.findViewById(R.id.ingredient_name), v, ingredient);
                ingredient.setSelected(!isSelect);
                notifyDataSetChanged();
            }
        });
    }


    private boolean bindBackground(TextView textView, View view, IngredientItemDO ingredientItemDO){
        boolean isSelected = ingredientItemDO.isSelected();
        if(isSelected) {
            view.setBackground(context.getResources().getDrawable(R.drawable.pressed));
            textView.setTextColor(Color.WHITE);
        }else{
            view.setBackground(context.getResources().getDrawable(R.drawable.normal));
            textView.setTextColor(Color.BLACK);
        }
        return isSelected;
    }

    @Override
    protected int getMaxIndexerLength() {
        return 1;
    }
}