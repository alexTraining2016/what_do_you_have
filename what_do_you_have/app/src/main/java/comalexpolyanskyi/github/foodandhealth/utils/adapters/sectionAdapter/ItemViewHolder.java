package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter;

import android.view.View;
import android.widget.TextView;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.ViewHolder;

public class ItemViewHolder extends ViewHolder {

    public final TextView name;

    public ItemViewHolder(View rootView) {
        super(rootView);
        name = findWidgetById(R.id.ingredient_name);
    }
}