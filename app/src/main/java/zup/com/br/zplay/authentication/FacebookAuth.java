package zup.com.br.zplay.authentication;

import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.util.Arrays;

import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.SupportActivity;

public class FacebookAuth extends AuthLogin {

    public static final int REQUEST_CODE_FACEBOOK = 64206;
    private SupportActivity activity;

    private FacebookAuth(SupportActivity activity) {
        super(activity);
        this.activity = activity;
    }

    public static FacebookAuth init(SupportActivity activity) {
        return new FacebookAuth(activity);
    }

    @Override
    public void connect() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFirebase(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, R.string.login_cancelado, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, activity.getString(R.string.algo_errado_message, error.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public AuthCredential getCredential(String idToken) {
        return FacebookAuthProvider.getCredential(idToken);
    }
}
