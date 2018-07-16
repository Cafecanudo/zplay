package zup.com.br.zplay.adapters;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            holder.setImagePoster(movie.getUrl_post());

            String rating = movie.getRank().replaceAll("[^0-9]", "");
            int rank = !"".equals(rating) ? Integer.valueOf(rating) : 0;

            for (int y = 0; y < (rank > 5 ? 5 : rank); y++) {
                ImageView imageView = new ImageView(activity);
                imageView.setImageResource(R.drawable.ic_star);
                holder.rank.addView(imageView);
                holder.rank.setBackground(null);
            }
        } else {
            holder.title.setText(null);
            holder.title.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.director.setText(null);
            holder.director.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.year.setText(null);
            holder.year.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.plot.setText(null);
            holder.plot.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.time.setText(null);
            holder.time.setBackgroundResource(R.drawable.bg_sckeleton_screen);

            holder.progressBar.setVisibility(View.VISIBLE);
            holder.url_post.setVisibility(View.GONE);

            holder.rank.removeAllViews();
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardviewImage)
        public View cardviewImage;

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

        public void setImagePoster(String url_post) {
            if (url_post != null) {
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
                cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL);
            }

            @Override
            public void removeCallback(SizeReadyCallback cb) {
            }
        };
    }
}
