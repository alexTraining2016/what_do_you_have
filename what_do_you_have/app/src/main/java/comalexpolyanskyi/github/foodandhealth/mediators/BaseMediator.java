package comalexpolyanskyi.github.foodandhealth.mediators;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class BaseMediator<T, P> implements InteractionContract.Mediator<P>, InteractionContract.RequiredPresenter<T> {

    private InteractionContract.RequiredView<T> view;

    public BaseMediator(@NonNull InteractionContract.RequiredView<T> view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadData(P... parameters) {
        view.showProgress(true);
    }

    @Override
    public void search(P... searchParameter) {
    }

    @Override
    public void onError() {
        if (view != null) {
            view.showProgress(false);
            view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
        }
    }

    @Override
    public void onSuccess(T request) {
        if (view != null) {
            view.showProgress(false);
            view.returnData(request);
        }
    }
}