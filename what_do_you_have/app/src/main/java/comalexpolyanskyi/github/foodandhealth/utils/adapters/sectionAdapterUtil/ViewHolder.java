package comalexpolyanskyi.github.foodandhealth.utils.adapters.sectionAdapterUtil;

import android.view.View;

public abstract class ViewHolder {
    public final View rootView;

    public ViewHolder(View rootView) {
        this.rootView = rootView;
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findWidgetById(int resId) {
        return (T) rootView.findViewById(resId);
    }
}