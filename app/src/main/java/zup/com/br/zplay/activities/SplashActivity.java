package zup.com.br.zplay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import zup.com.br.zplay.R;

public class SplashActivity extends SupportActivity {

    private static int TEMPO_SPLASH = 2000;

    @BindView(R.id.layoutSwipe)
    public LinearLayout swipeLayout;

    @BindView(R.id.imgZPlayLogo)
    public ImageView imgZPlayLogo;

    @BindView(R.id.imgZPlayName)
    public ImageView imgZPlayName;

    @Override
    int layoutID() {
        return R.layout.activity_splash;
    }

    @Override
    void inicializar(Bundle savedInstanceState) {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        this.ativarSplash();
        this.criandoObjetoTransicao();
    }

    private ActivityOptionsCompat criandoObjetoTransicao() {
        Pair<View, String> imgZPlayLogoPair = Pair.create(imgZPlayLogo, imgZPlayLogo.getTransitionName());
        Pair<View, String> imgZPlayNamePair = Pair.create(imgZPlayName, imgZPlayName.getTransitionName());
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, imgZPlayLogoPair, imgZPlayNamePair);
    }

    private void ativarSplash() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i, criandoObjetoTransicao().toBundle());
            finish();
        }, TEMPO_SPLASH);
    }
}
