package zup.com.br.zplay.authentication;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zup.com.br.zplay.activities.LoginActivity;
import zup.com.br.zplay.activities.MainActivity;
import zup.com.br.zplay.activities.SupportActivity;

import static zup.com.br.zplay.authentication.FacebookAuth.REQUEST_CODE_FACEBOOK;
import static zup.com.br.zplay.authentication.GoogleAuth.REQUEST_CODE_GOOGLE;

public abstract class AuthLogin {

    protected SupportActivity activity;
    protected FirebaseAuth    auth;
    protected FirebaseUser    user;
    public    CallbackManager callbackManager;

    public abstract void connect();

    public abstract AuthCredential getCredential(String idToken);

    protected AuthLogin(SupportActivity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }

    /**
     * Conectar no fireBase
     *
     * @param idToken
     */
    protected final void accessFirebase(String idToken) {
        AuthCredential credential = this.getCredential(idToken);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                user = auth.getCurrentUser();
                goToMain(activity);
            }
        });
    }

    public void onActivityResult(int requestCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                this.accessFirebase(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, -1, data);
        }
    }

    /**
     * Verifica se app esta logado no firebase
     *
     * @param activity
     */
    public static void checkLogin(SupportActivity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            goToMain(activity);
        } else {
            return;
        }
    }

    /**
     * Efetua o logout do firebase
     */
    public static void logout(SupportActivity activity) {
        FirebaseAuth.getInstance().signOut();

        //facebook
        LoginManager.getInstance().logOut();

        retornarLogin(activity);
    }

    /**
     * Chama a tela principal
     *
     * @param activity
     */
    protected static final void goToMain(SupportActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Retorna para pagina de login
     *
     * @param activity
     */
    public static void retornarLogin(SupportActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}