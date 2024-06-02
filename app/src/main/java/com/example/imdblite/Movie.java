package com.example.imdblite;



import com.google.gson.annotations.SerializedName;

public class Movie {
    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private double vote_average;
    private String backdrop_path;
    private String release_date;
    private boolean isFavorite;

    // Add this method to get full image URL
    public String getPosterUrl() {
        return "https://image.tmdb.org/t/p/w500" + poster_path;
    }

    public String getBackdropUrl() {
        return "https://image.tmdb.org/t/p/w500" + backdrop_path;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
    // Add isFavorite() method
    public boolean isFavorite() {
        return isFavorite;
    }

    // Add setFavorite() method
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}