package comalexpolyanskyi.github.foodandhealth.controllers;

/**
 * Created by Алексей on 13.10.2016.
 */

public interface InteractionContract {
    interface View {
        void showData(String data);
        void showError(String message);
        void showProgress(boolean isInProgress);
    }

    interface Controller {
        void getAllRecipesData();
    }
}