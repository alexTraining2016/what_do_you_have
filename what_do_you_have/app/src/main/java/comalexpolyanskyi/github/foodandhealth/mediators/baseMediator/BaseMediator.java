package comalexpolyanskyi.github.foodandhealth.mediators.baseMediator;

import android.support.annotation.NonNull;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ParametersInformationRequest;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class BaseMediator<T, P> implements InteractionContract.Mediator<P>, InteractionContract.RequiredPresenter<T> {

    private InteractionContract.RequiredView<T> view;
    private final static String noDataErrorMessage = ContextHolder.getContext().getString(R.string.error_loading);
    protected InteractionContract.DAO<ParametersInformationRequest> dao;

    public BaseMediator(@NonNull InteractionContract.RequiredView<T> view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        view = null;
        dao = null;
    }

    @Override
    public void loadData(P... parameters) {
        if(view != null) {
            view.showProgress(true);
        }
    }

    @Override
    public void search(P... searchParameter) {
    }

    @Override
    public void onError() {
        if (view != null) {
            view.showProgress(false);
            view.returnError(noDataErrorMessage);
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