package zup.com.br.zplay.dialogs;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import net.sqlcipher.database.SQLiteConstraintException;

import java.net.SocketTimeoutException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.SupportActivity;
import zup.com.br.zplay.entities.MovieEntity;
import zup.com.br.zplay.services.OmdbapiService;
import zup.com.br.zplay.services.dtos.DetailMovieDTO;
import zup.com.br.zplay.utils.AppClient;

public class DialogMovieAdd extends Dialog {

    @BindView(R.id.progressBar)
    public LinearLayout progressBar;

    @BindView(R.id.rank)
    public LinearLayout rank;

    @BindView(R.id.cardviewImage)
    public View cardviewImage;

    @BindView(R.id.btnAdcionarFilme)
    public Button btnAdcionarFilme;

    @BindView(R.id.year)
    public TextView year;

    @BindView(R.id.time)
    public TextView time;

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.actors)
    public TextView actors;

    @BindView(R.id.director)
    public TextView director;

    @BindView(R.id.plot)
    public TextView plot;

    @BindView(R.id.url_post)
    public ImageView url_post;

    private Call<DetailMovieDTO> consultarFilme;
    private SupportActivity      activity;
    private DetailMovieDTO       detailMovie;

    private BaseTarget target = new BaseTarget<BitmapDrawable>() {
        @Override
        public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
            url_post.setImageBitmap(bitmap.getBitmap());
            progressBar.setVisibility(View.GONE);
            url_post.setVisibility(View.VISIBLE);

            btnAdcionarFilme.setAlpha(1F);
            btnAdcionarFilme.setEnabled(true);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap.getBitmap());
            bitmapDrawable.setGravity(Gravity.BOTTOM | Gravity.RIGHT | Gravity.FILL_HORIZONTAL);
            url_post.setBackground(bitmap);
        }

        @Override
        public void getSize(SizeReadyCallback cb) {
            cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
        }

        @Override
        public void removeCallback(SizeReadyCallback cb) {
        }
    };

    public DialogMovieAdd(@NonNull String omdbID, @NonNull SupportActivity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_movie_add, null);
        setContentView(view);

        ButterKnife.bind(this, view);

        btnAdcionarFilme.setAlpha(0.4F);
        btnAdcionarFilme.setEnabled(false);

        consultarDetalhesFilme(omdbID);
    }

    public static DialogMovieAdd show(@NonNull String omdbID, @NonNull SupportActivity activity) {
        DialogMovieAdd dialog = new DialogMovieAdd(omdbID, activity);
        dialog.show();
        return dialog;
    }

    @OnClick(R.id.btnAdcionarFilme)
    public void btnAdcionarFilmeOnClick() {
        MovieEntity movie = new MovieEntity();
        try {
            movie.setImdbId(detailMovie.getImdbID());
            movie.setType(detailMovie.getType());
            movie.setProduction(detailMovie.getProduction());
            movie.setTitle(detailMovie.getTitle());
            movie.setYear(detailMovie.getYear().replaceAll("[^0-9]", ""));
            movie.setTime(detailMovie.getRuntime());
            movie.setDirector(detailMovie.getDirector());
            movie.setWriter(detailMovie.getWriter());
            movie.setActors(detailMovie.getActors());
            movie.setUrl_post(detailMovie.getPoster());
            movie.setPlot(detailMovie.getPlot());
            movie.setScore(detailMovie.getMetascore());
            movie.setRank(detailMovie.getImdbRating());
            movie.setVotes(detailMovie.getImdbVotes());
            movie.setGenre(detailMovie.getGenre());
            movie.setLanguage(detailMovie.getLanguage());
            movie.setDataCadastro(new Date());

            movie.setId(activity.getDaoSession().getMovieEntityDao().insert(movie));
            Toast.makeText(DialogMovieAdd.this.getContext(), R.string.filme_added, Toast.LENGTH_LONG).show();

            dismiss();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(DialogMovieAdd.this.getContext(), R.string.movie_exist, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(DialogMovieAdd.this.getContext(), R.string.algo_errado, Toast.LENGTH_LONG).show();
        }
    }

    private void consultarDetalhesFilme(String omdbID) {
        OmdbapiService omdbapiService = AppClient.getClient().create(OmdbapiService.class);
        consultarFilme = omdbapiService.obterFilmePorID(omdbID);
        consultarFilme.enqueue(new Callback<DetailMovieDTO>() {
            @Override
            public void onResponse(Call<DetailMovieDTO> call, Response<DetailMovieDTO> response) {
                if (response.isSuccessful() && response.body().getResponse().equalsIgnoreCase("true")) {
                    populateMovie(response.body());
                } else {
                    String error = activity.getString(R.string.algo_errado_message, response.body().getError());
                    Toast.makeText(DialogMovieAdd.this.getContext(), error, Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<DetailMovieDTO> call, Throwable t) {
                dismiss();
                if (call.isExecuted()) {
                    return;
                }
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(DialogMovieAdd.this.getContext(), R.string.conexao_lenta, Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(DialogMovieAdd.this.getContext(), R.string.algo_errado, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateMovie(DetailMovieDTO detailMovie) {
        this.detailMovie = detailMovie;

        year.setText(detailMovie.getYear().replaceAll("[^0-9]+", ""));
        year.setBackground(null);

        time.setText(detailMovie.getRuntime());
        time.setBackgroundResource(R.drawable.bg_item_movie_time);

        title.setText(detailMovie.getTitle());
        title.setBackground(null);

        actors.setText(detailMovie.getActors());
        actors.setBackground(null);

        director.setText(detailMovie.getDirector());
        director.setBackground(null);

        plot.setText(detailMovie.getPlot());
        plot.setBackground(null);

        String rating = detailMovie.getImdbRating().replaceAll("[^0-9]", "");
        int rank = !"".equals(rating) ? Integer.valueOf(rating) : 0;

        for (int y = 0; y < (rank > 5 ? 5 : rank); y++) {
            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(R.drawable.ic_star);
            this.rank.addView(imageView);
            this.rank.setBackground(null);
        }

        setImagePoster(detailMovie.getPoster());
    }

    private void setImagePoster(String url_post) {
        if (url_post != null && !url_post.equals("N/A")) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.url_post.setVisibility(View.GONE);
            Glide.with(activity).load(url_post).into(target);
        } else {
            this.cardviewImage.setVisibility(View.GONE);
            btnAdcionarFilme.setAlpha(1F);
            btnAdcionarFilme.setEnabled(true);
        }
    }
}
