package comalexpolyanskyi.github.foodandhealth.presenter;

public interface MVPContract {

    interface RequiredView<T> {
        void returnData(T response);
        void returnError(String message);
        void showProgress(boolean isInProgress);
    }

    interface Presenter<P, V> {
        void onConfigurationChanged(RequiredView<V> view);
        void onDestroy();
        void loadData(P... parameters);
        void search(P... searchParameter);
    }

    interface DAO<P> {
        void get(P parameters, boolean flag, boolean forceUpdate);
    }

    interface RequiredPresenter<T>{
        void onError();
        void onSuccess(T response);
    }
}