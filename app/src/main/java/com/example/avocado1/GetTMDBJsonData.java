package com.example.avocado1;

import android.os.AsyncTask;
import android.util.Log;
import android.net.Uri;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetTMDBJsonData extends AsyncTask<String, Void, List<Movie>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetTMDBJsonData";
    private List<Movie> movieList = null;
    private String baseUrl;
    private String language;

    private final OnDataReady callBack;
  // private final OnDataReadyTv callBacktv;
    private boolean runningOnSameThread = false;


    interface OnDataReady{
        void onDataReady(List data, DownloadStatus status);
    }



    public GetTMDBJsonData(OnDataReady callBack , String baseUrl, String language) {
        Log.d(TAG, "GetTMDBJsonData: called");
        this.baseUrl = baseUrl;
        this.language = language;
        this.callBack = callBack;
       // this.callBacktv= callBacktv;


    }

    void excuteOnSameThread(String searchCritiria){
        Log.d(TAG, "excuteOnSameThread: starts");
        runningOnSameThread = true;
        String destUri = createUri(language);

        GetRawData grd = new GetRawData(this);
        grd.execute(destUri);
        Log.d(TAG, "excuteOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        Log.d(TAG, "onPostExecute: starts");
        if(callBack != null){
            callBack.onDataReady(movieList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destURII = createUri(language);
        
        GetRawData grd = new GetRawData(this);
        grd.runInSameThread(destURII);
        Log.d(TAG, "doInBackground: ends");
        
        return movieList;
    }

    private String createUri(String language){

//        return Uri.parse(baseUrl).buildUpon()
//                .appendQueryParameter("lang", language).build().toString();
        return baseUrl;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus downloadStatus) {
        Log.d(TAG, "onDownloadComplete status: "+ downloadStatus);

        if(downloadStatus == DownloadStatus.OK){
            movieList = new ArrayList<>();
            try{
                JSONObject jData = new JSONObject(data);
                JSONArray resultsArray = jData.getJSONArray("results");

                for(int i=0; i<resultsArray.length(); i++){

                    JSONObject jMovie = resultsArray.getJSONObject(i);
                    String title = jMovie.getString("title");
                    String vote_avg = jMovie.getString("vote_average");
                    String overview = jMovie.getString("overview");
                    String trailer = jMovie.getString("video");
                    String release_date = jMovie.getString("release_date");
                    String poster = jMovie.getString("poster_path");

                    String posterLink = poster.replace("/", "https://image.tmdb.org/t/p/w92/");
                    // Example of Lion King poster request ==>  https://image.tmdb.org/t/p/original/fILTFOc4uV1mYL0qkoc3LyG1Jo9.jpg
                    // Discover size poster request ==>  https://image.tmdb.org/t/p/w92/fILTFOc4uV1mYL0qkoc3LyG1Jo9.jpg

                    Movie movie = new Movie(trailer, vote_avg , title, posterLink ,overview, release_date );
                    movieList.add(movie);

                    Log.d(TAG, "onDownloadComplete: " + movie.toString());
                }
            }catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + e.getMessage() );
                downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        //callback is only called if running on same thread
        if (runningOnSameThread && callBack != null){
            //notify processing is done
            callBack.onDataReady(movieList, downloadStatus);
        }

        Log.d(TAG, "onDownloadComplete: ends");

    }
}
