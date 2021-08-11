package com.example.finalprojects21;
/**
 *@Preeti Kumari
 *@version1.0
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecycleViewAdapter extends RecyclerView.Adapter<MovieRecycleViewAdapter.ViewHolder> {

    private List<Movie> movieList;
    private LayoutInflater layoutInflater;
    private Context context;
    private MyMovieThread.MovieListener movieListener;

    MovieRecycleViewAdapter(Context context , List<Movie> list, MyMovieThread.MovieListener listener){
        layoutInflater = LayoutInflater.from(context);
        this.movieList = list;
        this.context = context;
        this.movieListener = listener;
    }

    @NonNull
    @Override
    public MovieRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycleview_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MovieRecycleViewAdapter.ViewHolder holder, int position) {

            Movie movie = movieList.get(position);
            holder.movieTitle.setText(movie.getTile());
            holder.movieYear.setText(movie.getYear());

            String imageUrl = movie.getPosterUrl();
            if(!TextUtils.isEmpty(imageUrl)){
                Picasso.with(context)
                        .load(imageUrl)
                        .fit()
                        // To prevent fade animation
                        .noFade()
                        .into(holder.posterImage);
            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = movieList.get(position);
                MyMovieThread myMovieThread = new MyMovieThread(movieListener, context);
                myMovieThread.executeAsyncTask(movie.getTile(), false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        TextView movieTitle;
        TextView movieYear;
        ImageView posterImage;

        public TextView getMovieTitle() {
            return movieTitle;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieYear = (TextView) itemView.findViewById(R.id.movie_year);
            posterImage = (ImageView) itemView.findViewById(R.id.movie_image);

        }

    }

}
