package comalexpolyanskyi.github.foodandhealth.controllers;

import android.support.annotation.NonNull;

public class MainController implements InteractionContract.Controller, DataManager.DataManagerCallback {

    private InteractionContract.View view;

    public MainController(@NonNull InteractionContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllRecipesData() {
        view.showProgress(true);
        String url = "";
        loadData(url);
    }

    private void loadData(String url) {
        DataManager dataManager = new DataManager(url, this);
        dataManager.execute();
    }

    @Override
    public void onDataManagerError(String message) {

    }

    @Override
    public void onDataManagerSuccess(String result) {

    }
}