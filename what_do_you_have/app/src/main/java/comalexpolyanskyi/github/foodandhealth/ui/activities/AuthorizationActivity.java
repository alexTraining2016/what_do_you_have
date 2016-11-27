package comalexpolyanskyi.github.foodandhealth.ui.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthData;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthHelper;

public class AuthorizationActivity extends AppCompatActivity implements AuthHelper.AuthHelperCallback<AuthData> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void authSuccess(AuthData authData) {
    }
}
