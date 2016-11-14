package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class BasePresenter<T, P> implements MVPContract.Presenter<P, T>, MVPContract.RequiredPresenter<T> {

    private MVPContract.RequiredView<T> view;

    public BasePresenter(@NonNull MVPContract.RequiredView<T> view) {
        this.view = view;
    }

    @Override
    public void onConfigurationChanged(MVPContract.RequiredView<T> view)
    {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void loadData(P parameters) {
        view.showProgress(true);
    }

    @Override
    public void search(P searchParameter) {
    }

    @Override
    public void onError() {
        if(view != null){
            view.showProgress(false);
            view.returnError(ContextHolder.getContext().getString(R.string.error_loading));
        }
    }

    @Override
    public void onSuccess(T request) {
        if(view != null) {
            view.showProgress(false);
            view.returnData(request);
        }
    }
}