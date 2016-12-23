package comalexpolyanskyi.github.foodandhealth.utils.auth;

import android.os.AsyncTask;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;

public class GoogleAuthHelper implements AuthHelper<GoogleSignInResult, AuthData> {

    private static final String UTF_8 = "UTF-8";
    private static final String EMAIL = "&email=";
    private static final String LOGIN = "&login=";
    private AuthHelper.AuthHelperCallback<AuthData> helperCallback;

    @Override
    public void registerOnServer(final GoogleSignInResult result, final AuthHelperCallback<AuthData> helperCallback) {
        this.helperCallback = helperCallback;
        final GoogleSignInAccount account = result.getSignInAccount();
        if (account != null) {
            final String email = account.getEmail();
            final String login = account.getDisplayName();
            toSend(email, login);
        } else {
            helperCallback.onError(null);
        }
    }

    private AuthData processRequest(final String requestString) {
        final Type type = new TypeToken<List<AuthData>>() {
        }.getType();
        final Gson gson = new GsonBuilder().create();
        List<AuthData> authDatas =  gson.fromJson(requestString, type);

        return authDatas.get(0);
    }

    private void toSend(final String email, final String login) {
        new AsyncTask<String, Void, AuthData>() {

            @Override
            protected AuthData doInBackground(final String... params) {
                try {
                    final String urlParameters = EMAIL + URLEncoder.encode(params[0], UTF_8) +
                            LOGIN + URLEncoder.encode(params[1], UTF_8);
                    final AppHttpClient appHttpClient = AppHttpClient.getAppHttpClient();
                    final byte[] requestBytes = appHttpClient.loadDataFromHttp(AuthConstant.AUTH_API, urlParameters);
                    if (requestBytes != null) {
                        final String requestString = new String(requestBytes, Charset.forName(UTF_8));
                        return processRequest(requestString);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    helperCallback.onError(null);
                }
                return null;
            }

            @Override
            protected void onPostExecute(final AuthData response) {
                super.onPostExecute(response);
                if (response != null) {
                    helperCallback.authSuccess(response);
                } else {
                    helperCallback.onError(null);
                }
            }
        }.execute(email, login);
    }
}
