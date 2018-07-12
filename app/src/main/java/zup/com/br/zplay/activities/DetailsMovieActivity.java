package zup.com.br.zplay.activities;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.greendao.query.QueryBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import zup.com.br.zplay.R;
import zup.com.br.zplay.entities.MovieEntity;
import zup.com.br.zplay.entities.MovieEntityDao;

public class DetailsMovieActivity extends SupportActivity {

    private MovieEntityDao movieEntityDao;

    @BindView(R.id.backgroundPoster)
    public View backgroundPoster;

    @BindView(R.id.progressBar)
    public View progressBar;

    @BindView(R.id.cardviewImage)
    public View cardviewImage;

    @BindView(R.id.url_post)
    public ImageView url_post;

    @BindView(R.id.rank)
    public LinearLayout rank;

    @BindView(R.id.time)
    public TextView time;

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.plot)
    public TextView plot;

    @BindView(R.id.actors)
    public TextView actors;

    @BindView(R.id.director)
    public TextView director;

    @BindView(R.id.year)
    public TextView year;

    private BaseTarget target = new BaseTarget<BitmapDrawable>() {
        @Override
        public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
            url_post.setImageBitmap(bitmap.getBitmap());
            progressBar.setVisibility(View.GONE);
            url_post.setVisibility(View.VISIBLE);

            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap.getBitmap());
            bitmapDrawable.setGravity(Gravity.BOTTOM | Gravity.RIGHT | Gravity.FILL_HORIZONTAL);
            backgroundPoster.setBackground(bitmap);
        }

        @Override
        public void getSize(SizeReadyCallback cb) {
            cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
        }

        @Override
        public void removeCallback(SizeReadyCallback cb) {
        }
    };

    @Override
    int layoutID() {
        return R.layout.activity_details_movie;
    }

    private MovieEntity movie;

    @Override
    void inicializar(Bundle savedInstanceState) {
        Long id = (Long) this.getIntent().getExtras().get("MOVIE_ID");
        if (id != null) {
            this.movieEntityDao = this.getDaoSession().getMovieEntityDao();
            QueryBuilder<MovieEntity> query = this.movieEntityDao.queryBuilder();
            this.movie = query.where(MovieEntityDao.Properties.Id.eq(id)).unique();
            if (this.movie != null) {
                this.populateText();
            } else {
                Toast.makeText(this, R.string.registro_nao_encontrado, Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.algo_errado, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void populateText() {
        this.time.setText(movie.getTime());
        this.time.setBackgroundResource(R.drawable.bg_item_movie_time);

        this.title.setText(movie.getTitle());
        this.title.setBackground(null);

        this.director.setText("by " + movie.getDirector());
        this.director.setBackground(null);

        this.year.setText(movie.getYear());
        this.year.setBackground(null);

        this.plot.setText(movie.getPlot());
        this.plot.setBackground(null);

        this.actors.setText(movie.getActors());
        this.actors.setBackground(null);

        for (int y = 0; y < (movie.getRank() > 5 ? 5 : movie.getRank()); y++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_star);
            this.rank.addView(imageView);
            this.rank.setBackground(null);
        }

        this.setImagePoster(movie.getUrl_post());
    }

    private void setImagePoster(String url_post) {
        if (url_post != null) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.url_post.setVisibility(View.GONE);
            Glide.with(this).load(url_post).into(target);
        } else {
            this.cardviewImage.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnBack)
    public void btnBackOnClick() {
        finish();
    }
}
