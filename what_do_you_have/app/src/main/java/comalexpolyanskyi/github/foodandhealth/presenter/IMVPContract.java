package comalexpolyanskyi.github.foodandhealth.presenter;

public interface IMVPContract {

    interface RequiredView<T> {
        void returnData(T response);
        void returnError(String message);
        void showProgress(boolean isInProgress);
    }

    interface Presenter<P> {
        void onConfigurationChanged(RequiredView view);
        void onDestroy();
        void loadData(P parameters);
    }

    interface Model {
        void getData(String url);
        void onDestroy();
    }

    interface RequiredPresenter<T>{
        void onError();
        void onSuccess(T response);
    }
}