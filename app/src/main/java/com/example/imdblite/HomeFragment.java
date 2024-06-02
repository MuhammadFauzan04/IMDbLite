package com.example.imdblite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView rvTop10;
    private RecyclerView rvRecommended;
    private MovieAdapter top10Adapter;
    private MovieAdapter recommendedAdapter;
    private List<Movie> top10List = new ArrayList<>();
    private List<Movie> recommendedList = new ArrayList<>();
    private final String API_KEY = "9b90634d04f82b9cb342f6e4943db131";
    private LottieAnimationView progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        rvTop10 = view.findViewById(R.id.rv_popular);
        rvTop10.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        top10Adapter = new MovieAdapter(getContext(), top10List);
        rvTop10.setAdapter(top10Adapter);

        rvRecommended = view.findViewById(R.id.rv_topRated);
        rvRecommended.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recommendedAdapter = new MovieAdapter(getContext(), recommendedList);
        rvRecommended.setAdapter(recommendedAdapter);

        progressBar = view.findViewById(R.id.progress_bar);

        progressBar.playAnimation();

        fetchTop10Movies();
        fetchRecommendedMovies();

        return view;
    }

    private void fetchTop10Movies() {
        progressBar.setVisibility(View.VISIBLE);
        TMDbApi apiService = RetrofitInstance.getRetrofitInstance().create(TMDbApi.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    top10List.clear();
                    top10List.addAll(response.body().getMovies());
                    top10Adapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Response not successful: " + response.message());
                    Toast.makeText(getContext(), "Response not successful: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e("HomeFragment", "Error fetching data", t);
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecommendedMovies() {
        progressBar.setVisibility(View.VISIBLE);
        TMDbApi apiService = RetrofitInstance.getRetrofitInstance().create(TMDbApi.class);
        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    recommendedList.clear();
                    recommendedList.addAll(response.body().getMovies());
                    recommendedAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Response not successful: " + response.message());
                    Toast.makeText(getContext(), "Response not successful: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e("HomeFragment", "Error fetching data", t);
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}