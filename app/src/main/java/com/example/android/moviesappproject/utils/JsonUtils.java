package com.example.android.moviesappproject.utils;

import com.example.android.moviesappproject.model.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public JsonUtils() {
    }


    private final static String KEY_MOVIE_ARRAY = "results";
    private final static String KEY_NUM_PAGES = "total_pages";


    private final Gson gson = new Gson();

    public List<Movie> readMoviesFromJson(JSONObject json){
            List<Movie> movies = new ArrayList<>();
            JsonObject jsonObject = gson.fromJson(json.toString(), JsonObject.class);
        if (jsonObject.has(KEY_MOVIE_ARRAY)) {
            JsonArray results = jsonObject.getAsJsonArray(KEY_MOVIE_ARRAY);
            for (JsonElement element : results) {
                JsonObject movie = gson.fromJson(element,JsonObject.class);
                movies.add(gson.fromJson(movie,Movie.class));

            }

        }

        return movies;

    }

    public int getPageCount(JSONObject json){
        int pageCount = 0;
        JsonObject jsonObject = gson.fromJson(json.toString(), JsonObject.class);
        if (jsonObject.has(KEY_NUM_PAGES)){
            pageCount = jsonObject.get(KEY_NUM_PAGES).getAsInt();
        }

        return pageCount;
    }



}
