package comalexpolyanskyi.github.foodandhealth.ui.buttonManagers;

import android.support.design.widget.Snackbar;
import android.view.View;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers.AbstractButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class FavoritesButtonManager extends AbstractButtonManager {

    private static String TYPE = "repost";

    public FavoritesButtonManager(final View view, final DataUpdateCallback updateCallback) {
        super(view, TYPE, updateCallback);
    }

    @Override
    public void selectDrawable(final VectorImageTextView view, final boolean isChecked, final boolean isUpdateState) {
        if (isUpdateState) {
            makeActFavoriteMessage(isChecked, view);
        }
        if (isChecked) {
            view.setRightDrawable(R.drawable.ic_repeat_blue_24dp);
        } else {
            view.setRightDrawable(R.drawable.ic_repeat_black_24dp);
        }
    }

    private void makeActFavoriteMessage(boolean isChecked, VectorImageTextView view) {
        int str = R.string.favoritesDeleteMessage;
        if (isChecked) {
            str = R.string.favoritesAddMessage;
        }
        final String message = ContextHolder.getContext().getString(str);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(TYPE, null).show();
    }
}
