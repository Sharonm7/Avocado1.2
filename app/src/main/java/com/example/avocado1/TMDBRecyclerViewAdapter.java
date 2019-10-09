package com.example.avocado1;

import android.content.Context;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;


class TMDBRecyclerViewAdapter extends RecyclerView.Adapter<TMDBRecyclerViewAdapter.TMDBMovieHolder>{

    private static final String TAG = "TMDBRecyclerViewAdapter";
    private List<Movie> mMoviesList;
    private Context mContext;

    public TMDBRecyclerViewAdapter(Context context, List<Movie> moviesList) {
        mContext = context;
        this.mMoviesList = moviesList;
    }

    @NonNull
    @Override
    public TMDBMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Called by the layout manager when it needs new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover, parent, false);
        return new TMDBMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMDBMovieHolder holder, int position) {
        // called by the layout manager when it wants new data in an existing row
        Movie movieItem = mMoviesList.get(position);
        Log.d(TAG, "onBindViewHolder: " + movieItem.getTitle() + "==>" + position);
        Picasso.get().load(movieItem.getPoster_path())
                     .error(R.drawable.baseline_broken_image_black_48dp)
                     .placeholder(R.drawable.baseline_broken_image_black_48dp)
                     .placeholder(R.drawable.baseline_broken_image_black_48dp)
                     .into(holder.movie);

        holder.title.setText(movieItem.getTitle());
        //holder.overview.setText(movieItem.getOverview());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: called");
        return ((mMoviesList != null) && (mMoviesList.size() != 0) ? mMoviesList.size() : 0);
    }

    void loadNewData(List<Movie> newMovies){
        mMoviesList = newMovies;
        notifyDataSetChanged();
    }

//    public Movie getMovieIntoPosition{
//        return ((mMoviesList != null) && (mMoviesList.size() != 0) ? mMoviesList.get(position) : null);
//    }

    static class TMDBMovieHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "TMDBMovieHolder";
        ImageView movie = null;
        TextView title = null;
        //TextView overview = null;

        public TMDBMovieHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "TMDBMovieHolder: starts");
            this.movie = (ImageView)itemView.findViewById(R.id.thumbnail);
            this.title = (TextView)itemView.findViewById(R.id.title);
            //this.overview = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
