package comalexpolyanskyi.github.foodandhealth.ui.activities.buttonManagers;

import android.support.design.widget.Snackbar;
import android.view.View;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.mediators.DAButtonMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ViewStateHolder;

abstract public class AbstractButtonManager implements View.OnClickListener, InteractionContract.RequiredView<Void> {

    private ViewStateHolder oldViewStateHolder;
    private VectorImageTextView view;
    public static final String ACTION = "Action";
    private InteractionContract.Mediator<String> mediator;
    public static String TYPE;
    private String token, id;

    public AbstractButtonManager(final String token, final String id, final View view, final boolean isLike, final String initValue) {
        mediator = new DAButtonMediator(this);
        this.token = token;
        this.id = id;
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
        oldViewStateHolder = new ViewStateHolder(view.getRightDrawable(), label, isLike);
        selectDrawable(this.view, !isLike, true);
        updateText(!isLike, label);
        view.setTag(R.string.likeState, !isLike);
        mediator.loadData(id, token, TYPE);
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

    abstract void selectDrawable(final VectorImageTextView view, final boolean isLike, final boolean isUpdateState);

    @Override
    public void returnError(final String message) {
        view.setRightDrawable(oldViewStateHolder.getDrawable());
        view.setText(oldViewStateHolder.getLabel());
        view.setTag(R.string.likeState, oldViewStateHolder.isLike());
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
    }


    @Override
    public void returnData(Void response) {
    }
}
