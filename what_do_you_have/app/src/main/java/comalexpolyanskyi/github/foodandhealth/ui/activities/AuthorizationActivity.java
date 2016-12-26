package comalexpolyanskyi.github.foodandhealth.ui.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthData;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthHelper;
import comalexpolyanskyi.github.foodandhealth.utils.auth.GoogleAuthHelper;

public class AuthorizationActivity extends AppCompatActivity implements AuthHelper.AuthHelperCallback<AuthData>, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 10;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int rgbColor = determineBackground();
        setContentView(R.layout.activity_authorization);
        findViewById(R.id.auth_root_layout).setBackgroundColor(rgbColor);
        configureGoogleSignIn();
        final SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);
        final View withoutRegButton = findViewById(R.id.without_auth_button);
        withoutRegButton.setOnClickListener(this);
    }

    private int determineBackground() {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.signin_image);
        final Palette palette = Palette.from(bitmap).generate();
        final Palette.Swatch swatch = palette.getDarkVibrantSwatch();

        if (swatch != null) {
            return swatch.getRgb();
        }else {
            return ContextCompat.getColor(this, R.color.colorPrimaryFood);
        }
    }

    @Override
    public void authSuccess(AuthData authData) {
        Intent intent = new Intent();
        intent.putExtra(AuthConstant.AUTH_DATA_KEY, authData);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onError(final String message) {
        if (message != null) {
            showErrorMessage(message);
        } else {
            showErrorMessage(getString(R.string.server_error));
        }
    }

    public void showErrorMessage(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(
                getString(R.string.ok_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void showWarningMessage(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(
                R.string.ok_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        authSuccess(new AuthData(AuthConstant.ANONYMOUS, AuthConstant.ANONYMOUS, AuthConstant.ANONYMOUS));
                    }
                });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.without_auth_button:
                showWarningMessage(getString(R.string.not_authorized));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            AuthHelper<GoogleSignInResult, AuthData> helper = new GoogleAuthHelper();
            helper.registerOnServer(result, this);
        }
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onError(connectionResult.getErrorMessage() + connectionResult.getErrorCode());
    }
}
