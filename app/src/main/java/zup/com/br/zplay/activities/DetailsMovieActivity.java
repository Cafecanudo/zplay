package zup.com.br.zplay.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import zup.com.br.zplay.R;

public class DetailsMovieActivity extends SupportActivity {

    @BindView(R.id.imagem)
    public ImageView imagem;

    @BindView(R.id.nome)
    public TextView nome;

    @BindView(R.id.autor)
    public TextView autor;

    @BindView(R.id.elenco)
    public TextView elenco;

    @BindView(R.id.ano)
    public TextView ano;

    @BindView(R.id.sinopse)
    public TextView sinopse;

    @BindView(R.id.tempo)
    public TextView tempo;

    @BindView(R.id.rank)
    public LinearLayout rank;

    @Override
    int layoutID() {
        return R.layout.activity_details_movie;
    }

    @Override
    void inicializar(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btnBack)
    public void btnBackOnClick() {
        finish();
    }
}
