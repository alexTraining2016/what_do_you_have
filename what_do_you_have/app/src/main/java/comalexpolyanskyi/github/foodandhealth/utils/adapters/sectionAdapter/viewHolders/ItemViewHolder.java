package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter.viewHolders;

import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.ViewHolder;

public class ItemViewHolder extends ViewHolder {

    public final TextView name;
    public final AppCompatImageView checkedView;
    public final ImageView background;

    public ItemViewHolder(View rootView) {
        super(rootView);
        name = findWidgetById(R.id.ingredient_name);
        checkedView = findWidgetById(R.id.checked_image);
        background = findWidgetById(R.id.background_image);
    }
}