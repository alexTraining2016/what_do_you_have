package comalexpolyanskyi.github.foodandhealth.mediators;

public interface InteractionContract {

    interface RequiredView<T> {

        void returnData(T response);

        void returnError(String message);

        void showProgress(boolean isInProgress);
    }

    interface Mediator<P> {

        void onDestroy();

        boolean accessCheck(String token);

        void loadData(P... parameters);

        void search(P... searchParameter);
    }

    interface DAO<P> {

        void get(P parameters, boolean forceUpdate, boolean noUpdate);

        void update(P parameters);

        void onDestroy();
    }

    interface RequiredPresenter<T> {

        void onError();

        void onSuccess(T response);
    }
}