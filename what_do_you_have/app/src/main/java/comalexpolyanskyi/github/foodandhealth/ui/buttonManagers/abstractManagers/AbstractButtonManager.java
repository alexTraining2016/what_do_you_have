package comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers;

import android.support.design.widget.Snackbar;
import android.view.View;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.mediators.DAButtonMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;

abstract public class AbstractButtonManager implements View.OnClickListener, InteractionContract.RequiredView<Void> {

    private VectorImageTextView view;
    private static final String ACTION = "Action";
    private InteractionContract.Mediator<String> mediator;
    private String type;
    private String token, id;
    private DataUpdateCallback updateCallback;

    public interface DataUpdateCallback {
        void refresh();
    }

    public AbstractButtonManager(final String token, final String id, final View view, final boolean isLike,
                                 final String initValue, final String type, final DataUpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
        mediator = new DAButtonMediator(this);
        this.token = token;
        this.id = id;
        this.view = (VectorImageTextView) view;
        this.view.setOnClickListener(this);
        this.view.setText(initValue);
        this.view.setTag(R.string.likeState, isLike);
        this.type = type;
        selectDrawable(this.view, isLike, false);
    }

    @Override
    public void onClick(final View v) {
        view = (VectorImageTextView) v;
        final Boolean isLike = (Boolean) view.getTag(R.string.likeState);
        final String label = view.getText().toString();
        selectDrawable(this.view, !isLike, true);
        updateText(!isLike, label);
        view.setTag(R.string.likeState, !isLike);
        mediator.loadData(id, token, type);
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

    protected abstract void selectDrawable(final VectorImageTextView view, final boolean isLike, final boolean isUpdateState);

    @Override
    public void returnError(final String message) {
        updateCallback.refresh();
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
    }


    @Override
    public void returnData(Void response) {
        updateCallback.refresh();
    }
}
