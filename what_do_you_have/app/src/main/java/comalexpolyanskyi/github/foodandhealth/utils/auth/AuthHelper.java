package comalexpolyanskyi.github.foodandhealth.utils.auth;


public interface AuthHelper<A, T> {

    void registerOnServer(A result, AuthHelperCallback<T> callback);

    interface AuthHelperCallback<T>{
        void authSuccess(T data);
        void onError(String message);
    }
}
