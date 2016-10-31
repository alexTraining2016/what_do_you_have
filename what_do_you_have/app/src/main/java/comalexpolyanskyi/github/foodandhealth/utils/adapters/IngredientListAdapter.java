package comalexpolyanskyi.github.foodandhealth.utils.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.pojo.IngredientItemModel;

public class IngredientListAdapter extends ArrayAdapter<IngredientItemModel> implements SectionIndexer {

    private HashMap<String, Integer> mapIndex;
    private String[] sections;
    private List<IngredientItemModel> data;

    public IngredientListAdapter(Context context, List<IngredientItemModel> data, int layout) {
        super(context, layout, data);
        this.data = data;
        installFastScroll();
    }

    private void installFastScroll(){
        mapIndex = new LinkedHashMap<>();
        for (int x = 0; x < data.size(); x++) {
            IngredientItemModel itemModel = data.get(x);
            String ch = itemModel.getName().substring(0, 1);
            ch = ch.toUpperCase(Locale.US);
            mapIndex.put(ch, x);
        }
        Set<String> sectionLetters = mapIndex.keySet();
        ArrayList<String> sectionList = new ArrayList<>(sectionLetters);
        Collections.sort(sectionList);
        sections = new String[sectionList.size()];
        sectionList.toArray(sections);
    }

    public void updateDataSet()
    {
        notifyDataSetChanged();
        installFastScroll();
    }

    public int getPositionForSection(int section) {
        if(section > 1){
            return mapIndex.get(sections[section-1]);
        }else{
            return mapIndex.get(sections[section]);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        } else {
            view = convertView;
        }
        bindView(position, view);
        return view;
    }

    private boolean bindBackground(TextView textView, View view, IngredientItemModel ingredientItemModel){
        boolean isSelected = ingredientItemModel.isSelected();
        if(isSelected) {
            view.setBackground(getContext().getResources().getDrawable(R.drawable.pressed));
            textView.setTextColor(Color.WHITE);
        }else{
            view.setBackground(getContext().getResources().getDrawable(R.drawable.normal));
            textView.setTextColor(Color.BLACK);
        }
        return isSelected;
    }

    private void bindView(final int position, View view) {
        final IngredientItemModel ingredient = getItem(position);
        view.findViewById(R.id.ingredient_name).setTag(ingredient.getId());
        TextView textView = ((TextView) view.findViewById(R.id.ingredient_name));
        textView.setText(ingredient.getName());
        bindBackground(textView, view, ingredient);
        view.setOnClickListener(new View.OnClickListener() {
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

    public int getSectionForPosition(int position) {
        return 0;
    }

    public Object[] getSections() {
        return sections;
    }
}