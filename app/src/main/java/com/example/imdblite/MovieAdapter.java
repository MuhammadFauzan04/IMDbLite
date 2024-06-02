package com.example.imdblite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ExecutorService executorService;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.gson = new Gson();
        this.executorService = Executors.newSingleThreadExecutor(); // Thread pool with single thread
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        Glide.with(context).load(movie.getPosterUrl()).into(holder.ivPoster);

        boolean isFavorite = checkIfFavorite(movie.getId());
        movie.setFavorite(isFavorite);

        int favoriteIcon = movie.isFavorite() ? R.drawable.favoritefilled : R.drawable.favorite;
        holder.favoriteButton.setImageResource(favoriteIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to start MovieDetail activity
                Intent intent = new Intent(context, MovieDetail.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("poster_path", movie.getPosterUrl());
                intent.putExtra("backdrop_path", movie.getBackdropUrl());
                intent.putExtra("vote_average", movie.getVoteAverage());
                intent.putExtra("release_date", movie.getRelease_date());
                intent.putExtra("movie_id", movie.getId()); // Add movie_id

                // Start the activity
                context.startActivity(intent);
            }
        });

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = movie.isFavorite();

                if (isFavorite) {
                    removeFavorite(movie);
                    showToast("Removed from favorites");
                } else {
                    addFavorite(movie);
                    showToast("Added to favorites");
                }

                movie.setFavorite(!isFavorite);

                int newFavoriteIcon = movie.isFavorite() ? R.drawable.favoritefilled : R.drawable.favorite;
                holder.favoriteButton.setImageResource(newFavoriteIcon);

                if (context instanceof ProfileFragment.ProfileFragmentListener) {
                    ((ProfileFragment.ProfileFragmentListener) context).onFavoriteUpdated();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvVoteAverage;
        ImageView ivPoster, favoriteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvVoteAverage = itemView.findViewById(R.id.tv_rate);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);
        }
    }


    private boolean checkIfFavorite(int movieId) {
        return sharedPreferences.getBoolean(String.valueOf(movieId), false);
    }

    private void addFavorite(Movie movie) {
        editor.putBoolean(String.valueOf(movie.getId()), true);
        editor.apply();
        updateFavoriteMovies(movie, true);
    }

    private void removeFavorite(Movie movie) {
        editor.putBoolean(String.valueOf(movie.getId()), false);
        editor.apply();
        updateFavoriteMovies(movie, false);
    }

    private void updateFavoriteMovies(Movie movie, boolean add) {
        List<Movie> favoriteMovies = getFavoriteMoviesFromSharedPreferences();
        if (add) {
            favoriteMovies.add(movie);
        } else {
            for (int i = 0; i < favoriteMovies.size(); i++) {
                if (favoriteMovies.get(i).getId() == movie.getId()) {
                    favoriteMovies.remove(i);
                    break;
                }
            }
        }
        String jsonFavorites = gson.toJson(favoriteMovies);
        editor.putString("favorite_movies", jsonFavorites);
        editor.apply();
    }

    private List<Movie> getFavoriteMoviesFromSharedPreferences() {
        String jsonFavorites = sharedPreferences.getString("favorite_movies", null);
        if (jsonFavorites != null) {
            Type type = new TypeToken<List<Movie>>() {}.getType();
            return gson.fromJson(jsonFavorites, type);
        } else {
            return new ArrayList<>();
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Metode onPause() yang disarankan untuk menghapus pesan tertunda dari thread pool
    public void onPause() {
        executorService.shutdown();
    }
}
