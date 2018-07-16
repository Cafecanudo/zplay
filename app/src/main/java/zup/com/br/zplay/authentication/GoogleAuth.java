package zup.com.br.zplay.authentication;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.SupportActivity;

public class GoogleAuth extends AuthLogin {

    public static final int REQUEST_CODE_GOOGLE = 90;
    private GoogleSignInClient mGoogleSignInClient;

    private GoogleAuth(SupportActivity activity) {
        super(activity);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public static GoogleAuth init(SupportActivity activity) {
        return new GoogleAuth(activity);
    }

    @Override
    public void connect() {
        activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_GOOGLE);
    }

    @Override
    public AuthCredential getCredential(String idToken) {
        return GoogleAuthProvider.getCredential(idToken, null);
    }
}
