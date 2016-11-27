package comalexpolyanskyi.github.foodandhealth.utils.auth;


public interface AuthHelper {

    void signIn();
    void signUp();

    interface AuthHelperCallback<T>{
        void authSuccess(T data);
    }
}
