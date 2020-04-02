package com.example.android.moviesappproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moviesappproject.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> movies;
    private final MoviesOnClickHandler onClickHandler;

/*    public MoviesAdapter() {

    }*/

    public MoviesAdapter(MoviesOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    void setMovies(List<Movie> movies) {
        this.movies = movies;
    }



    public interface MoviesOnClickHandler{
       void onClick(Movie movie);
    }



    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);

        return new MoviesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Picasso picasso = Picasso.get();
            picasso
                .load(movies.get(position).getImage())
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);

}

    @Override
    public int getItemCount() {
        if (movies ==null){
            return 0;
        }else{
        return movies.size();
        }
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        final ImageView imageView;


        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            onClickHandler.onClick(movies.get(adapterPosition));
        }
    }
}
