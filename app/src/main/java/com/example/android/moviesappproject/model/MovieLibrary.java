package com.example.android.moviesappproject.model;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieLibrary {

    private final List<Movie> movieList = new ArrayList<>();
    private int pageCount;
    private int pagesLoaded;
    private boolean isPopularMoviesSelected = true;
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String POPULAR_MOVIES = "movie/popular";
    private final static String TOP_RATED_MOVIES = "movie/top_rated";
    private final String API_KEY;

    public MovieLibrary( int pagesLoaded, String apiKey) {
        this.API_KEY = apiKey;
        this.pagesLoaded = pagesLoaded;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }


    public int getPageCount() {
        return pageCount;
    }

    public int getPagesLoaded() {
        return pagesLoaded;
    }


    public void addPage() {
        pagesLoaded++;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPopularMoviesSelected(boolean popularMoviesSelected) {
        isPopularMoviesSelected = popularMoviesSelected;
    }

    public String getMoviesUrl(){
        String movieRanking;
        if(isPopularMoviesSelected){
            movieRanking = POPULAR_MOVIES;
        }else {movieRanking = TOP_RATED_MOVIES;}

        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(movieRanking)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page",String.valueOf(pagesLoaded))
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

    public void resetMovieLibrary(){
        movieList.clear();
        pagesLoaded=1;
    }

}
