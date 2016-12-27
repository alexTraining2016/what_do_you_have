package comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers;

import android.view.View;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;

abstract public class AbstractButtonManager implements View.OnClickListener {

    private VectorImageTextView view;
    private DataUpdateCallback updateCallback;
    private String type;

    public interface DataUpdateCallback {

        void refresh(String type);
    }

    public AbstractButtonManager(final View view, final String type, final DataUpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
        this.type = type;
        this.view = (VectorImageTextView) view;
        this.view.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        view = (VectorImageTextView) v;
        final Boolean isLike = (Boolean) view.getTag(R.string.likeState);
        final String label = view.getText().toString();
        this.selectDrawable(this.view, !isLike, false);
        this.updateText(!isLike, label);
        this.view.setTag(R.string.likeState, !isLike);
        updateCallback.refresh(type);
    }

    public void setData(final String initValue, boolean isLike) {
        this.resetText(initValue);
        this.view.setTag(R.string.likeState, isLike);
        this.selectDrawable(this.view, isLike, false);
    }

    private void updateText(boolean isLike, String label) {
        if (isLike) {
            int intLabel = Integer.parseInt(label);
            intLabel++;
            this.view.setText(Integer.toString(intLabel));
        } else {
            int intLabel = Integer.parseInt(label);
            intLabel--;
            this.view.setText(Integer.toString(intLabel));
        }
    }

    public void setOnClickListener(final View.OnClickListener clickListener) {
        this.view.setOnClickListener(clickListener);
    }

    public void resetText(final String text) {
        view.setText(text);
    }

    public void resetDrawable(boolean isLike, boolean showMessage) {
        selectDrawable(this.view, isLike, showMessage);
    }

    public abstract void selectDrawable(final VectorImageTextView view, final boolean isLike, final boolean isUpdateState);
}
