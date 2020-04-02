package com.example.android.moviesappproject;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private final static String EXTRA_TITLE =  "title";
    private final static String EXTRA_ORIGINAL_TITLE = "original_title";
    private final static String EXTRA_IMAGE = "image";
    private final static String EXTRA_RATING = "vote_average";
    private final static String EXTRA_DATE_RELEASED = "date_released";
    private final static String EXTRA_OVERVIEW = "overview";
    private Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        res = getResources();
        bindViews();


    }



    private void bindViews(){
        ImageView imageView = findViewById(R.id.detail_image);
        RatingBar ratingBar = findViewById(R.id.detail_rating_bar);
        TextView originalTitle = findViewById(R.id.detail_original_title);
        TextView rating = findViewById(R.id.detail_rating_value);
        TextView dateReleased = findViewById(R.id.detail_date_released);
        TextView overview = findViewById(R.id.detail_description);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_TITLE)){
            setTitle(intent.getStringExtra(EXTRA_TITLE));
        }
        if(intent.hasExtra(EXTRA_ORIGINAL_TITLE)){
            originalTitle.setText(res.getString(R.string.original_title, intent.getStringExtra(EXTRA_ORIGINAL_TITLE)));
        }
        if(intent.hasExtra(EXTRA_IMAGE)){
            Picasso.get()
                    .load(intent.getStringExtra(EXTRA_IMAGE))
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);

        }
        if(intent.hasExtra(EXTRA_RATING)){
            String ratingValue = intent.getStringExtra(EXTRA_RATING);
            rating.setText(res.getString(R.string.rating, ratingValue));
            ratingBar.setRating(Float.parseFloat(ratingValue)/2);

        }
        if(intent.hasExtra(EXTRA_DATE_RELEASED)){
            dateReleased.setText(res.getString(R.string.date_released, intent.getStringExtra(EXTRA_DATE_RELEASED)));
        }
        if(intent.hasExtra(EXTRA_OVERVIEW)){
            overview.setText(intent.getStringExtra(EXTRA_OVERVIEW));
        }
    }






}
