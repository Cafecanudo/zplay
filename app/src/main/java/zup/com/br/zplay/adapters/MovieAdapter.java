package zup.com.br.zplay.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.DetailsMovieActivity;
import zup.com.br.zplay.activities.SupportActivity;
import zup.com.br.zplay.entities.MovieEntity;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Activity          activity;
    private List<MovieEntity> movieList;

    public MovieAdapter(SupportActivity activity, List<MovieEntity> movieList) {
        this.activity = activity;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_movie, viewGroup, false);
        itemView.setOnClickListener(view -> {

            View url_post = view.findViewById(R.id.url_post);

            Pair<View, String> url_postPair = Pair.create(url_post, url_post.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, url_postPair);

            Intent intent = new Intent(activity, DetailsMovieActivity.class);

            MovieEntity movie = movieList.get(i);
            intent.putExtra("MOVIE_ID", movie.getId());
            activity.startActivity(intent, options.toBundle());
            activity.startActivity(intent);
        });
        ButterKnife.bind(this, itemView);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int i) {
        MovieEntity movie = movieList.get(i);
        if (movie.getId() != null) {
            holder.title.setText(movie.getTitle());
            holder.title.setBackground(null);

            holder.director.setText(movie.getDirector());
            holder.director.setBackground(null);

            holder.year.setText(movie.getYear());
            holder.year.setBackground(null);

            holder.plot.setText(movie.getPlot());
            holder.plot.setBackground(null);

            holder.time.setText(movie.getTime());
            holder.time.setBackgroundResource(R.drawable.bg_item_movie_time);

            for (int y = 0; y < movie.getRank(); y++) {
                ImageView imageView = new ImageView(activity);
                imageView.setImageResource(R.drawable.ic_star);
                holder.rank.addView(imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        public View progressBar;

        @BindView(R.id.url_post)
        public ImageView url_post;

        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.director)
        public TextView director;

        @BindView(R.id.year)
        public TextView year;

        @BindView(R.id.plot)
        public TextView plot;

        @BindView(R.id.time)
        public TextView time;

        @BindView(R.id.rank)
        public LinearLayout rank;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
