package zup.com.br.zplay.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import zup.com.br.zplay.R;
import zup.com.br.zplay.adapters.MovieAdapter;
import zup.com.br.zplay.authentication.AuthLogin;
import zup.com.br.zplay.dialogs.DialogSearch;
import zup.com.br.zplay.entities.MovieEntity;
import zup.com.br.zplay.entities.MovieEntityDao;
import zup.com.br.zplay.utils.RecyclerItemClickListener;

public class MainActivity extends SupportActivity {

    private MovieEntityDao movieEntityDao;

    @BindView(R.id.textSearch)
    public EditText textSearch;

    @BindView(R.id.textNoFound)
    public TextView textNoFound;

    @BindView(R.id.listMovie)
    public RecyclerView listMovie;

    @BindView(R.id.coordinatorLayout)
    public View coordinatorLayout;

    @BindView(R.id.btnExitSearch)
    public View btnExitSearch;

    @BindView(R.id.btnImgSearch)
    public View btnImgSearch;

    @BindView(R.id.noFoundFileText)
    public View noFoundFileText;

    private MovieAdapter      movieAdapter;
    private List<MovieEntity> movieList;

    @Override
    int layoutID() {
        return R.layout.activity_main;
    }

    @Override
    void inicializar(Bundle savedInstanceState) {
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        listMovie.setLayoutManager(mLayoutManager);
        listMovie.setItemAnimator(new DefaultItemAnimator());
        listMovie.setAdapter(movieAdapter);

        listMovie.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
            MovieEntity movie = movieList.get(position);

            View url_post = view.findViewById(R.id.url_post);
            View title = view.findViewById(R.id.title);
            View rank = view.findViewById(R.id.rank);

            Pair<View, String> url_postPair = Pair.create(url_post, url_post.getTransitionName());
            Pair<View, String> titlePair = Pair.create(title, title.getTransitionName());
            Pair<View, String> rankPair = Pair.create(rank, rank.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    url_postPair, titlePair, rankPair);

            Intent intent = new Intent(this, DetailsMovieActivity.class);

            intent.putExtra("MOVIE_ID", movie.getId());
            startActivity(intent, options.toBundle());
        }));

        this.movieEntityDao = this.getDaoSession().getMovieEntityDao();

        //Add evento editext
        this.textSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.pesquisar();
                return false;
            }
            return false;
        });

        this.prepareList();
    }

    /**
     * Pesquisar registro no banco de dados
     */
    private void pesquisar() {
        String text = this.textSearch.getText().toString();
        if (text.length() >= 4) {
            addSckeletonScreen();

            this.resetViews();

            this.btnExitSearch.setVisibility(View.VISIBLE);
            this.btnImgSearch.setVisibility(View.GONE);

            new Handler().postDelayed(() -> {

                QueryBuilder<MovieEntity> query = this.movieEntityDao.queryBuilder();
                query.where(
                        query.or(
                                MovieEntityDao.Properties.Title.like(text),
                                MovieEntityDao.Properties.Actors.like(text),
                                MovieEntityDao.Properties.Director.like(text),
                                MovieEntityDao.Properties.Genre.like(text),
                                MovieEntityDao.Properties.Plot.like(text),
                                MovieEntityDao.Properties.Production.like(text),
                                MovieEntityDao.Properties.Language.like(text)
                        ));
                query.orderAsc(MovieEntityDao.Properties.Title);
                List<MovieEntity> list = query.list();
                movieList.clear();
                if (!list.isEmpty()) {
                    movieList.addAll(list);
                    movieAdapter.notifyDataSetChanged();

                    listMovie.setVisibility(View.VISIBLE);
                    noFoundFileText.setVisibility(View.GONE);
                } else {
                    textNoFound.setText(R.string.no_found_data_search);
                    listMovie.setVisibility(View.GONE);
                    noFoundFileText.setVisibility(View.VISIBLE);
                }
            }, 3000);
        } else {
            Snackbar.make(coordinatorLayout, R.string.char_min_4, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void prepareList() {
        this.addSckeletonScreen();
        this.resetViews();

        new Handler().postDelayed(() -> {
            List<MovieEntity> list = this.movieEntityDao.queryBuilder()
                    .orderAsc(MovieEntityDao.Properties.Title)
                    .list();

            movieList.clear();
            if (!list.isEmpty()) {
                movieList.addAll(list);
                movieAdapter.notifyDataSetChanged();
            } else {
                textNoFound.setText(R.string.no_data_saved);
                noFoundFileText.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    private void addSckeletonScreen() {
        movieList.clear();
        movieList.add(new MovieEntity());
        movieAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btnImgSearch, R.id.btnImgAdd, R.id.btnImgUser, R.id.btnExitSearch})
    public void actions(View view) {
        switch (view.getId()) {
            case R.id.btnImgAdd: {
                this.adicionarNovoFilme();
                break;
            }
            case R.id.btnImgSearch: {
                this.pesquisar();
                break;
            }
            case R.id.btnExitSearch: {
                this.textSearch.setText("");
                this.prepareList();
                break;
            }
            case R.id.btnImgUser: {
                AuthLogin.logout(this);
                break;
            }
        }
    }

    private void resetViews() {
        this.btnExitSearch.setVisibility(View.GONE);
        this.btnImgSearch.setVisibility(View.VISIBLE);

        listMovie.setVisibility(View.VISIBLE);
        noFoundFileText.setVisibility(View.GONE);
    }

    private void adicionarNovoFilme() {
        DialogSearch.show(this).setOnDismissListener(dialogInterface -> this.prepareList());
    }
}
