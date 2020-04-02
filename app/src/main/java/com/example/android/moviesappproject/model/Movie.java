package com.example.android.moviesappproject.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class Movie {

    private final static String IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    private final static String IMG_BASE_SIZE = "w342";
    @SerializedName("poster_path")
    private String  imagePath;
    @SerializedName("title")
    private String  title;
    @SerializedName("original_title")
    private String  originalTitle;
    @SerializedName("overview")
    private String  overview ;
    @SerializedName("vote_average")
    private String  voteAverage;
    @SerializedName("release_date")
    private String  releaseDate;

    public Movie() {
    }

    public Movie(String imagePath, String title, String originalTitle, String overview, String voteAverage, String releaseDate) {
        this.imagePath = imagePath;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }
    
    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getImage() {


        return getImage(IMG_BASE_SIZE);
    }
    public String getImage(String imgSize) {
        Uri uri = Uri.parse(IMG_BASE_URL)
                .buildUpon()
                .appendPath(imgSize)
                .appendEncodedPath(imagePath)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        return url.toString();
    }

}
