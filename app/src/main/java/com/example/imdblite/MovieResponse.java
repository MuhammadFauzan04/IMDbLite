package com.example.imdblite;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {
    private int page;
    @SerializedName("results") // Gunakan anotasi ini jika nama propertinya berbeda dengan yang diharapkan
    private List<Movie> movies; // Ubah nama properti menjadi 'movies'

    // Getters and setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() { // Ubah nama metode menjadi 'getMovies'
        return movies;
    }

    public void setMovies(List<Movie> movies) { // Ubah nama metode menjadi 'setMovies'
        this.movies = movies;
    }

    public List<Movie> getResults() {
        return movies;
    }
}
