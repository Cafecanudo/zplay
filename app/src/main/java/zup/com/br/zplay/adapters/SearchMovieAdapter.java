package zup.com.br.zplay.adapters;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zup.com.br.zplay.R;
import zup.com.br.zplay.activities.SupportActivity;
import zup.com.br.zplay.dialogs.DialogMovieAdd;
import zup.com.br.zplay.services.dtos.MovieSearchDTO;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.MovieViewHolder> {

    private SupportActivity      activity;
    private List<MovieSearchDTO> movieList;

    public SearchMovieAdapter(SupportActivity activity, List<MovieSearchDTO> movieList) {
        this.activity = activity;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_movie_search_omdbapi, viewGroup, false);
        ButterKnife.bind(this, itemView);
        return new MovieViewHolder(movieList.get(i), itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int i) {
        MovieSearchDTO movie = movieList.get(i);
        if (movie.getImdbID() != null) {
            holder.title.setText(movie.getTitle());
            holder.title.setBackground(null);

            holder.type.setText(movie.getType().equals("movie") ? "Filme" : "Series");
            holder.type.setBackground(null);

            holder.year.setText(movie.getYear().replaceAll("[^0-9]+", ""));
            holder.year.setBackground(null);

            holder.setImagePoster(movie.getPoster());
        } else {
            holder.title.setText(null);
            holder.title.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.type.setText(null);
            holder.type.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.year.setText(null);
            holder.year.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.progressBar.setVisibility(View.VISIBLE);
            holder.url_post.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cardviewImage)
        public View cardviewImage;

        @BindView(R.id.progressBar)
        public View progressBar;

        @BindView(R.id.url_post)
        public ImageView url_post;

        @BindView(R.id.type)
        public TextView type;

        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.year)
        public TextView year;

        private MovieSearchDTO movie;

        public MovieViewHolder(MovieSearchDTO movie, View view) {
            super(view);
            this.movie = movie;
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        public void setImagePoster(String url_post) {
            if (url_post != null && !url_post.equals("N/A")) {
                this.progressBar.setVisibility(View.VISIBLE);
                this.url_post.setVisibility(View.GONE);
                Glide.with(activity).load(url_post).into(target);
            } else {
                this.cardviewImage.setVisibility(View.GONE);
            }
        }

        private BaseTarget target = new BaseTarget<BitmapDrawable>() {
            @Override
            public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
                url_post.setImageDrawable(bitmap);
                progressBar.setVisibility(View.GONE);
                url_post.setVisibility(View.VISIBLE);
            }

            @Override
            public void getSize(SizeReadyCallback cb) {
                cb.onSizeReady(120, 180);
            }

            @Override
            public void removeCallback(SizeReadyCallback cb) {
            }
        };

        @Override
        public void onClick(View view) {
            DialogMovieAdd.show(movie.getImdbID(), activity);
        }
    }
}
