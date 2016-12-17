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

    public AbstractButtonManager(final View view, final boolean isLike,
                                 final String initValue, final String type, final DataUpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
        this.type = type;
        this.view = (VectorImageTextView) view;
        this.view.setOnClickListener(this);
        this.view.setText(initValue);
        this.view.setTag(R.string.likeState, isLike);
        selectDrawable(this.view, isLike, false);
    }

    @Override
    public void onClick(final View v) {
        view = (VectorImageTextView) v;
        final Boolean isLike = (Boolean) view.getTag(R.string.likeState);
        final String label = view.getText().toString();
        selectDrawable(this.view, !isLike, false);
        updateText(!isLike, label);
        view.setTag(R.string.likeState, !isLike);
        updateCallback.refresh(type);
    }

    private void updateText(boolean isLike, String label) {
        if (isLike) {
            int intLabel = Integer.parseInt(label);
            intLabel++;
            view.setText(Integer.toString(intLabel));
        } else {
            int intLabel = Integer.parseInt(label);
            intLabel--;
            view.setText(Integer.toString(intLabel));
        }
    }
    public void setText(final String text){
        view.setText(text);
    }

    public void updateDrawable(final boolean isLike){
        selectDrawable(this.view, isLike, true);
    }

    public abstract void selectDrawable(final VectorImageTextView view, final boolean isLike, final boolean isUpdateState);
}
