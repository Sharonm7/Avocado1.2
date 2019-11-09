package com.example.avocado1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


import androidx.recyclerview.widget.RecyclerView;

public class TMDBTVRecyclerViewAdapter extends RecyclerView.Adapter<TMDBTVRecyclerViewAdapter.TMDBTVMovieHolder>{


    private static final String TAG = "TMDBTVRecyclerViewAdapter";
    private List<TvShow> mTvShowList;
    private Context mContext;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private List <String> followingTvShows= new ArrayList<>();


    public TMDBTVRecyclerViewAdapter(Context context, List<TvShow> tvShowList) {
        mContext = context;
        this.mTvShowList = tvShowList;
    }

    @NonNull
    @Override
    public TMDBTVRecyclerViewAdapter.TMDBTVMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        //Called by the layout manager when it needs new view
       Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_tvshow_detail, parent, false);
        return new TMDBTVRecyclerViewAdapter.TMDBTVMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TMDBTVRecyclerViewAdapter.TMDBTVMovieHolder holder, final int position) {
        // called by the layout manager when it wants new data in an existing row
        final TvShow tvShowItem = mTvShowList.get(position);
        //Log.d(TAG, "onBindViewHolder: " + tvShowItem.getTitle() + "==>" + position);
        Picasso.get().load(tvShowItem.getPoster_path())
                .error(R.drawable.baseline_broken_image_black_48dp)
                .placeholder(R.drawable.baseline_broken_image_black_48dp)
                .placeholder(R.drawable.baseline_broken_image_black_48dp)
                .into(holder.tvshowPoster);

        holder.title.setText(tvShowItem.getTitle());
        holder.overview.setText(tvShowItem.getOverview());
/*
        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "You Clicked the poster", Toast.LENGTH_LONG).show();
            }
        });


        holder.newMoviesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "You Clicked the layout ", Toast.LENGTH_LONG).show();
            }
        });
*/
        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tvshowId= 0;
                String tvshowTitle= null;
                String posterPath= null;
                String tvshowOverview= null;


                tvshowId= tvShowItem.getId();
                tvshowTitle= tvShowItem.getTitle();
                posterPath= tvShowItem.getPoster_path();
                tvshowOverview= tvShowItem.getOverview();

                followingTvShows.add(tvshowTitle);

                updateMoviesToDataBase(followingTvShows);


                Toast.makeText(mContext, "following list:"+followingTvShows, Toast.LENGTH_LONG).show();

            }
        });








    }



    @Override
    public int getItemCount() {
      //  Log.d(TAG, "getItemCount: called");
        return ((mTvShowList != null) && (mTvShowList.size() != 0) ? mTvShowList.size() : 0);
    }

    void loadNewData(List<TvShow> newTvShows){
        mTvShowList = newTvShows;
        notifyDataSetChanged();
    }

//    public Movie getMovieIntoPosition{
//        return ((mMoviesList != null) && (mMoviesList.size() != 0) ? mMoviesList.get(position) : null);
//    }

    static class TMDBTVMovieHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "TMDBMovieHolder";
        public LinearLayout newTvShowsLinearLayout;


        ImageView tvshowPoster = null;
        TextView title = null;
        TextView overview= null;
        Button followBtn;


        //TextView overview = null;

        public TMDBTVMovieHolder(@NonNull View itemView) {
            super(itemView);

            newTvShowsLinearLayout= (LinearLayout) itemView.findViewById(R.id.contenttvshowdetail);
            Log.d(TAG, "TMDBMovieHolder: starts");
            this.tvshowPoster = (ImageView)itemView.findViewById(R.id.moviePoster);
            this.title = (TextView)itemView.findViewById(R.id.movieTitle);
            this.overview = (TextView)itemView.findViewById(R.id.movieOverview);
            this.followBtn= (Button) itemView.findViewById(R.id.followBtn);


        }


    }

    private void updateMoviesToDataBase(final List <String> followingTvShows) {


        myRef = FirebaseDatabase.getInstance().getReference("Users");

        final String userName = mAuth.getCurrentUser().getDisplayName().toString();


        myRef.child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef.child(userName).child("followingTvShows").setValue(followingTvShows);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

