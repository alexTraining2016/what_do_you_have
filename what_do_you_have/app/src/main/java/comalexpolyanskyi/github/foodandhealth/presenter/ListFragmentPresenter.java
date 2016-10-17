package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.models.ListFragmentModel;
import comalexpolyanskyi.github.foodandhealth.models.beans.ListItemBean;
import comalexpolyanskyi.github.foodandhealth.utils.ContextHolder;

public class ListFragmentPresenter implements IMVPContract.Presenter, IMVPContract.RequiredPresenter<ListItemBean> {

    private IMVPContract.RequiredView view;
    private IMVPContract.Model model;

    public ListFragmentPresenter(@NonNull IMVPContract.RequiredView view) {
        this.view = view;
        this.model = new ListFragmentModel(this);
    }

    @Override
    public void onConfigurationChanged(IMVPContract.RequiredView view) {

    }

    @Override
    public void loadListItems(final String url) {
        view.showProgress(true);
        model.getListItems(url);
    }

    @Override
    public void onError() {
        view.showProgress(false);
        view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
    }

    @Override
    public void onSuccess(SparseArrayCompat<ListItemBean> response) {
        view.showProgress(false);
    }
}