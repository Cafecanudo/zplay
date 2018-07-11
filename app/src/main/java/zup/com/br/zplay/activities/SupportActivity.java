package zup.com.br.zplay.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import zup.com.br.zplay.entities.DaoSession;

public abstract class SupportActivity extends AppCompatActivity {

    private DaoSession daoSession;

    /**
     * Retorno o ID do layout
     *
     * @return
     */
    abstract int layoutID();

    /**
     * Init substitui on create
     *
     * @param savedInstanceState
     */
    abstract void inicializar(Bundle savedInstanceState);

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID());
        ButterKnife.bind(this);


        this.daoSession = ((App) getApplication()).getDaoSession();
        this.inicializar(savedInstanceState);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
