package com.example.finalprojects21;
/**
 *@Preeti Kumari
 *@version1.0
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MyMovieThread {
    private final static String TAG = "MyMovieThread";

    private MovieListener movieListener;
    public static MyMovieThread singleton;
    public boolean isList;
    private ProgressDialog dialog;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    public MyMovieThread(MovieListener movieListener, Context context ) {
        this.movieListener  = movieListener;
        dialog = new ProgressDialog(context);
    }



    public void executeAsyncTask(String movieTitle, boolean isList){
        this.isList = isList;
        MyTask myAsyncTask = new MyTask();
        myAsyncTask.execute(movieTitle);

    }

    public class MyTask extends AsyncTask<String,Void,String> {

        JSONObject jsonObject;
        String responseString;
        String stringURL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Searching, please wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String movieTitle = strings[0];

                Log.d(TAG," doInBackground movieTitle = "+movieTitle);

                if(isList){
                    stringURL = "https://www.omdbapi.com/?apikey=93c938a8&r=xmlt=&s="
                            + URLEncoder.encode(movieTitle, "UTF-8");
                }else{
                    stringURL = "https://www.omdbapi.com/?apikey=93c938a8&r=xmlt=&t="
                            + URLEncoder.encode(movieTitle, "UTF-8");
                }


                // Creates a URL object
                URL url = new URL(stringURL.toString());
                //connects to the server,
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //Waits for a response from the server.
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                responseString = (new BufferedReader(
                        new InputStreamReader(in, StandardCharsets.UTF_8)))
                        .lines()
                        .collect(Collectors.joining("\n"));

                //jsonObject = new JSONObject(text); //convert String to JASONObject

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if(!TextUtils.isEmpty(responseString)){
                parseMovieObject(responseString);
            }
        }

    }

    private void parseMovieObject(String responseString){

        if(isList){
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray jsonArray = jsonObject.getJSONArray("Search");
                if(jsonArray == null){
                    return;
                }
                List<Movie> movieList =  new ArrayList<Movie>();
                for(int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject arrayJsonObject  = jsonArray.getJSONObject(i);
                    String title = arrayJsonObject.getString("Title");
                    String year  = arrayJsonObject.getString("Year");
                    String rated = "No Info";
                    String released = "No Info";;
                    String posterUrl  = arrayJsonObject.getString("Poster");

                    Log.d(TAG," doInBackground jsonObject = "+jsonObject.toString());

                    Movie movie = new Movie();
                    movie.setTile(title);
                    movie.setYear(year);
                    movie.setRated(rated);
                    movie.setReleaseDate(released);
                    movie.setPosterUrl(posterUrl);
                    movieList.add(movie);
                }
                movieListener.listenMvoiecollection(movieList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                if(jsonObject == null){
                    return;
                }
                String title = jsonObject.getString("Title");
                String year  = jsonObject.getString("Year");
                String rated = jsonObject.getString("Rated");
                String released = jsonObject.getString("Released");
                String posterUrl  = jsonObject.getString("Poster");

                Log.d(TAG," doInBackground jsonObject = "+jsonObject.toString());

                Movie movie = new Movie();
                movie.setTile(title);
                movie.setYear(year);
                movie.setRated(rated);
                movie.setReleaseDate(released);
                movie.setPosterUrl(posterUrl);

                movieListener.openMovieDetail(movie);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface MovieListener {
       void listenMvoiecollection(List<Movie> movieList);
       void openMovieDetail(Movie movie);
    }

}
