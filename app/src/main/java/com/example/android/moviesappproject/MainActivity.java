package com.example.android.moviesappproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.moviesappproject.model.Movie;
import com.example.android.moviesappproject.model.MovieLibrary;
import com.example.android.moviesappproject.utils.JsonUtils;

import org.json.JSONObject;

import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickHandler{
    private MoviesAdapter moviesAdapter;
    private MovieLibrary movieLibrary;
    private RequestQueue queue;
    private static final int NUM_COLUMNS = 2;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        movieLibrary = new MovieLibrary(1, this.getString(R.string.api_key));
        moviesAdapter = new MoviesAdapter(this);


        final GridLayoutManager layoutManager;
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, NUM_COLUMNS, RecyclerView.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        }

        final RecyclerView movieRecyclerView = findViewById(R.id.movies_grid);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setAdapter(moviesAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                movieLibrary.resetMovieLibrary();
                makeApiCall(movieLibrary.getMoviesUrl());
                moviesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //idea for using onscrolllistener to detect end of recycler view has been taken from stackoverflow
        //it has been then reasearched by me in Docs on developer.android.com and implemented.
        movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                   if (layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount()-1){
                      if(movieLibrary.getPageCount()>movieLibrary.getPagesLoaded()){
                       movieLibrary.addPage();
                       if (toast!=null){
                           toast.cancel();
                       }
                       Toast.makeText(MainActivity.this, R.string.loading, Toast.LENGTH_SHORT).show();
                       makeApiCall(movieLibrary.getMoviesUrl());
                       moviesAdapter.notifyDataSetChanged();
                   }
                }
            }
        });

        makeApiCall(movieLibrary.getMoviesUrl());
        showMovies();
    }

    private void showMovies() {

        moviesAdapter.setMovies(movieLibrary.getMovieList());
    }

    private void makeApiCall(String uri) {

        //Part for connectivity check code taken from https://developer.android.com
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            JsonUtils jsonUtils = new JsonUtils();
                            List<Movie> movieList;
                            movieList = jsonUtils.readMoviesFromJson(response);
                            movieLibrary.setPageCount(jsonUtils.getPageCount(response));
                            if (movieList != null) {
                                movieLibrary.getMovieList().addAll(movieList);
                                moviesAdapter.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError e) {
                            e.printStackTrace();
                        }
                    });
            queue.add(jsonObjectRequest);
        }else{
            if (toast!=null){
                toast.cancel();
            }
            Toast.makeText(MainActivity.this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetail.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("title", movie.getTitle());
        intentToStartDetailActivity.putExtra("original_title", movie.getOriginalTitle());
        intentToStartDetailActivity.putExtra("image", movie.getImage());
        intentToStartDetailActivity.putExtra("vote_average", movie.getVoteAverage());
        intentToStartDetailActivity.putExtra("date_released", movie.getReleaseDate());
        intentToStartDetailActivity.putExtra("overview", movie.getOverview());
        startActivity(intentToStartDetailActivity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        movieLibrary.resetMovieLibrary();
        switch (item.getItemId()){

            case    R.id.menu_popular:
                movieLibrary.setPopularMoviesSelected(true);
                makeApiCall(movieLibrary.getMoviesUrl());
                return true;
            case R.id.menu_top_rated:
                movieLibrary.setPopularMoviesSelected(false);
                makeApiCall(movieLibrary.getMoviesUrl(
                ));
                 return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
