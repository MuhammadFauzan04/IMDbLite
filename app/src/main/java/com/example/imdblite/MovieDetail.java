package com.example.imdblite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetail extends AppCompatActivity {
    private static final String API_KEY = "52a18783ed514602a5facb15a0177e61";
    private TextView tvTitle, tvOverview, tvVoteAverage, tvReleaseDate;
    private ImageView ivPoster, ivBackdrop, ivBack;
    private RecyclerView recyclerViewCast;
    private CastAdapter castAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        tvTitle = findViewById(R.id.tv_title);
        tvOverview = findViewById(R.id.tv_overview);
        tvVoteAverage = findViewById(R.id.tv_rate);
        tvReleaseDate = findViewById(R.id.tv_release);
        ivPoster = findViewById(R.id.iv_poster);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        recyclerViewCast = findViewById(R.id.rv_cast);
        ivBack = findViewById(R.id.btn_back);

        recyclerViewCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castAdapter = new CastAdapter(this);
        recyclerViewCast.setAdapter(castAdapter);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tutup aktivitas saat ini dan kembali ke aktivitas sebelumnya
                finish();
            }
        });


        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        String posterPath = getIntent().getStringExtra("poster_path");
        String backdropPath = getIntent().getStringExtra("backdrop_path");
        double voteAverage = getIntent().getDoubleExtra("vote_average", 0);
        String releaseDate = getIntent().getStringExtra("release_date");
        int movieId = getIntent().getIntExtra("movie_id", 0);

        // Set data to views
        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvVoteAverage.setText(String.valueOf(voteAverage));
        tvReleaseDate.setText(releaseDate);
        Glide.with(this).load(posterPath).into(ivPoster);
        Glide.with(this).load(backdropPath).into(ivBackdrop);

        // Fetch cast data
        fetchMovieCredits(movieId);
    }

    private void fetchMovieCredits(int movieId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDbApi tmDbApi = retrofit.create(TMDbApi.class);
        Call<CreditsResponse> call = tmDbApi.getMovieCredits(movieId, API_KEY);

        call.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cast> castList = response.body().getCast();
                    List<Cast> limitedCastList = castList.subList(0, Math.min(castList.size(), 4));
                    castAdapter.setCastList(limitedCastList);
                }
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Log.e("MovieDetail", "Failed to fetch credits", t);
            }
        });
    }
}