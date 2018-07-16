package zup.com.br.zplay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;

import butterknife.BindView;
import butterknife.OnClick;
import zup.com.br.zplay.R;
import zup.com.br.zplay.authentication.AuthLogin;
import zup.com.br.zplay.authentication.FacebookAuth;
import zup.com.br.zplay.authentication.GoogleAuth;

import static zup.com.br.zplay.authentication.FacebookAuth.REQUEST_CODE_FACEBOOK;
import static zup.com.br.zplay.authentication.GoogleAuth.REQUEST_CODE_GOOGLE;

public class LoginActivity extends SupportActivity {

    @BindView(R.id.btnLoginFacebook)
    public View btnLoginFacebook;

    @BindView(R.id.btnLoginGoogle)
    public View btnLoginGoogle;

    private FacebookAuth facebookAuth;
    private GoogleAuth   googleAuth;

    @Override
    int layoutID() {
        return R.layout.activity_login;
    }

    @Override
    void inicializar(Bundle savedInstanceState) {
        startAnimation();
        googleAuth = GoogleAuth.init(this);
        facebookAuth = FacebookAuth.init(this);
    }

    @OnClick({R.id.btnLoginFacebook, R.id.btnLoginGoogle})
    public void btnLoginFacebookClick(View view) {
        if (view.getId() == R.id.btnLoginFacebook) {
            facebookAuth.connect();
        } else if (view.getId() == R.id.btnLoginGoogle) {
            googleAuth.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthLogin.checkLogin(this);
    }

    private void startAnimation() {
        this.btnLoginFacebook.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_login_facebook));
        this.btnLoginGoogle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.btn_login_google));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GOOGLE) {
            googleAuth.onActivityResult(requestCode, data);
        } else if (requestCode == REQUEST_CODE_FACEBOOK) {
            facebookAuth.onActivityResult(requestCode, data);
        }
    }
}
