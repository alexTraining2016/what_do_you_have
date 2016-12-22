package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthData;

public class StartActivity extends Activity {

    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = getSharedPreferences(AuthConstant.AUTH, MODE_PRIVATE);
        final AuthData authData = new AuthData(sharedPreferences.getString(AuthConstant.TOKEN, AuthConstant.EMPTY),
                sharedPreferences.getString(AuthConstant.NAME, AuthConstant.EMPTY),
                sharedPreferences.getString(AuthConstant.ID, AuthConstant.EMPTY));

        if (authData.isEmpty()) {
            startAuthorizationActivity();
        } else {
            startMainActivity(authData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final AuthData authData = (AuthData) data.getSerializableExtra(AuthConstant.AUTH_DATA_KEY);
            saveAuthData(authData);
            startMainActivity(authData);
            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }

    private void startAuthorizationActivity() {
        final Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void startMainActivity(final AuthData authData) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AuthConstant.TOKEN, authData.getToken());
        intent.putExtra(AuthConstant.NAME, authData.getName());
        intent.putExtra(AuthConstant.ID, authData.getId());
        startActivity(intent);
        finish();
    }

    private void saveAuthData(final AuthData authData) {
        final SharedPreferences preferences = getSharedPreferences(AuthConstant.AUTH, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AuthConstant.TOKEN, authData.getToken());
        editor.putString(AuthConstant.NAME, authData.getName());
        editor.putString(AuthConstant.ID, authData.getId());
        editor.apply();
    }
}
