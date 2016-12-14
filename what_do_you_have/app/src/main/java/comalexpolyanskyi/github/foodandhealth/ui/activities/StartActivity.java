package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;

public class StartActivity extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = getSharedPreferences(AuthConstant.AUTH, MODE_PRIVATE);
        final String token = sharedPreferences.getString(AuthConstant.TOKEN, AuthConstant.EMPTY);

        // if (token.equals(AuthConstant.EMPTY)) {
        //   final Intent intent = new Intent(this, AuthorizationActivity.class);
        //  startActivityForResult(intent, REQUEST_CODE);
        //} else {
        //  startMainActivity(token, sharedPreferences.getString(AuthConstant.NAME, AuthConstant.EMPTY));
        //}
        startMainActivity("1234", "Alex Polynskij", "1");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            final String token = data.getStringExtra(AuthConstant.TOKEN);
            final String name = data.getStringExtra(AuthConstant.NAME);
            final String id = data.getStringExtra(AuthConstant.ID);
            saveAuthData(token, name, id);
            startMainActivity(token, name, id);
        }
    }

    private void startMainActivity(final String token, final String name, final String id) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AuthConstant.TOKEN, token);
        intent.putExtra(AuthConstant.NAME, name);
        intent.putExtra(AuthConstant.ID, id);
        startActivity(intent);
    }

    private void saveAuthData(final String token, final String name, final String id) {
        SharedPreferences preferences = getSharedPreferences(AuthConstant.AUTH, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AuthConstant.TOKEN, token);
        editor.putString(AuthConstant.NAME, name);
        editor.putString(AuthConstant.ID, id);
        editor.apply();
    }
}
