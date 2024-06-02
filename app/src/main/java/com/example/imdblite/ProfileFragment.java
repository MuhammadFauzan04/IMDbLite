package com.example.imdblite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvFavorite;
    private MovieAdapter favoriteAdapter;
    private List<Movie> favoriteMovies;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private ProfileFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        rvFavorite = view.findViewById(R.id.rv_favorite);
        sharedPreferences = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        gson = new Gson();

        favoriteMovies = getFavoriteMoviesFromSharedPreferences();

        favoriteAdapter = new MovieAdapter(getContext(), favoriteMovies);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFavorite.setAdapter(favoriteAdapter);

        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        favoriteMovies = getFavoriteMoviesFromSharedPreferences();
        favoriteAdapter.setMovies(favoriteMovies);
    }

    public interface ProfileFragmentListener {
        void onFavoriteUpdated();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentListener) {
            listener = (ProfileFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void onFavoriteUpdated() {
        if (listener != null) {
            favoriteMovies = getFavoriteMoviesFromSharedPreferences();
            favoriteAdapter.setMovies(favoriteMovies);
        }
    }
}
