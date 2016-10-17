package comalexpolyanskyi.github.foodandhealth.presenter;

import android.support.v4.util.SparseArrayCompat;

import java.io.Serializable;

public interface IMVPContract {

    interface RequiredView<T extends Serializable> {
        void returnData(SparseArrayCompat<T> response);
        void returnError(String message);
        void showProgress(boolean isInProgress);
    }

    interface Presenter {
        void onConfigurationChanged(RequiredView view);
        void loadListItems(String url);
    }

    interface Model{
        void getListItems(String url);

    }

    interface RequiredPresenter<T extends Serializable>{
        void onError();
        void onSuccess(SparseArrayCompat<T> response);
    }
}