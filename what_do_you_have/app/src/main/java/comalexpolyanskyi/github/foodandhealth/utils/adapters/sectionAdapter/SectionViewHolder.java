package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapter;

import android.view.View;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil.ViewHolder;

public class SectionViewHolder extends ViewHolder {

    public final TextView textView;

    public SectionViewHolder(View rootView) {
        super(rootView);
        textView = (TextView) rootView;
    }
}