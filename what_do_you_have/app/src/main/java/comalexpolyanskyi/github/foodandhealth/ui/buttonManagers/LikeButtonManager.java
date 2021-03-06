package comalexpolyanskyi.github.foodandhealth.ui.buttonManagers;

import android.view.View;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers.AbstractButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;

public class LikeButtonManager extends AbstractButtonManager {

    private static String TYPE = "like";

    public LikeButtonManager(final View view, final DataUpdateCallback updateCallback) {
        super(view, TYPE, updateCallback);
    }

    @Override
    public void selectDrawable(final VectorImageTextView view, final boolean isLike, final boolean isUpdateState) {
        if (isLike) {
            view.setRightDrawable(R.drawable.ic_favorite_blue_24dp);
        } else {
            view.setRightDrawable(R.drawable.ic_favorite_black_24dp);
        }
    }
}
