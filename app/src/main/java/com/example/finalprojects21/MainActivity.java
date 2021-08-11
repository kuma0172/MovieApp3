package com.example.finalprojects21;
/**
 *@Preeti Kumari
 *@version1.0
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyMovieThread.MovieListener, TextWatcher {


    private EditText editSearchMovie;
    private Button searchButton;
    private RecyclerView movieRecycleView;
    private MovieRecycleViewAdapter myadapter;
    private TextView emptyList;
    private View loginInfo;


    //Account
    private TextView createAccount;
    private EditText userName;
    private EditText userPassword;
    private EditText reUserPassword;
    private Button submitButton;

    private List<Movie> movieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/**
 * EditSearch Movie Id is declared in the xml file of layout
 *  editSearchMovie Movie Id is declared in the xml file of layout
 *  searchButton  Movie Id is declared in the xml file of layout
 */
        editSearchMovie = (EditText) findViewById(R.id.search_edit_text);
        editSearchMovie.addTextChangedListener(this);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
/**
 * RecyclerView  to present items in a list. Selecting an item from the RecyclerView must show detailed
 * information about the item selected.
 */
        myadapter = new MovieRecycleViewAdapter(this, movieList, this);
        movieRecycleView = (RecyclerView) findViewById(R.id.movie_recycleview);
        movieRecycleView.setLayoutManager(new LinearLayoutManager(this));
        movieRecycleView.setAdapter(myadapter);

        DividerItemDecoration divider =
                new DividerItemDecoration(movieRecycleView.getContext(),
                        DividerItemDecoration.VERTICAL);

        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(),
                R.drawable.line_divide));

        movieRecycleView.addItemDecoration(divider);
        emptyList = (TextView) findViewById(R.id.movie_not_found);

        //Account
        createAccount = (TextView) findViewById(R.id.create_account);
        loginInfo = findViewById(R.id.login_info);
        userName = (EditText) findViewById(R.id.login_userName);
        userPassword = (EditText) findViewById(R.id.login_password);
        reUserPassword = (EditText) findViewById(R.id.login_password_re);
        submitButton = (Button) findViewById(R.id.submit_login_button);
        submitButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String userName = sh.getString("userName", "");
        if(TextUtils.isEmpty(userName)){
            reUserPassword.setVisibility(View.VISIBLE);
        }else{
            createAccount.setText(getString(R.string.please_login));
            reUserPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == searchButton.getId()) {
            String movieTitle = editSearchMovie.getText().toString();
            if (!TextUtils.isEmpty(movieTitle)) {

                MyMovieThread myMovieThread = new MyMovieThread(this, MainActivity.this);
                myMovieThread.executeAsyncTask(movieTitle, true);
            } else {
                /**
                 * Here is the AlertDialog which will alert user to take further action
                 * When user wants to buy a movie, an alert will pop up showing the price of the movie
                 */
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.error_empty_edit_text))

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }else if(id  == submitButton.getId()){
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String userNametext = sharedPreferences.getString("userName", "");
            if(TextUtils.isEmpty(userNametext)){
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                String name = userName.getText().toString();
                String pass = userPassword.getText().toString();
                String rePass = reUserPassword.getText().toString();
                if(TextUtils.isEmpty(name)  || TextUtils.isEmpty(pass) ||  TextUtils.isEmpty(rePass)){


                    /**
                     * ToastBar Notification is shown here. When user click on Buy
                     * User can see a popup of snackbar
                     */
                    Toast.makeText(this, getString(R.string.error),Toast.LENGTH_LONG).show();

                    /**
                     * Snackbar Notification is shown here. When user click on Buy
                     * User can see a popup of snackbar
                     */
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    myEdit.putString("userName", name);
                    myEdit.putString("password", pass);
                    myEdit.putString("rePassword", rePass);
                    loginInfo.setVisibility(View.GONE);
                    myEdit.apply();
                }
            }else{

                /**
                 * used SharedPreferences to save something about what was typed in the EditText
                 * for use the next time the application is launched.
                 * Here when we login for our emulator a sharedPreferences is used
                 */
                String userPasswordtext = sharedPreferences.getString("password", "");
                  if(userNametext.equalsIgnoreCase(userName.getText().toString()) && userPasswordtext.equalsIgnoreCase(userPassword.getText().toString())){
                      loginInfo.setVisibility(View.GONE);
                  }else{
                      Toast.makeText(this, getString(R.string.error),Toast.LENGTH_LONG).show();
                      Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error), Snackbar.LENGTH_LONG);
                      snackbar.show();
                  }

            }
        }

    }

    @Override
    public void listenMvoiecollection(List<Movie> list) {

        if (!movieList.isEmpty()) {
            movieList.clear();
        }

        if (!list.isEmpty()) {
            movieList.addAll(list);
            myadapter.notifyDataSetChanged();
            emptyList.setVisibility(View.GONE);
        }else{
            emptyList.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void openMovieDetail(Movie movie) {
        if (movie != null) {
            Toast.makeText(this, movie.getTile() + "clicked", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(this, MovieDetailActvity.class);
            myIntent.putExtra("Movie", movie);
            startActivity(myIntent);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String title = s.toString();
        if (!TextUtils.isEmpty(title)) {
            MyMovieThread myMovieThread = new MyMovieThread(this, MainActivity.this);
            myMovieThread.executeAsyncTask(title, true);
        }

    }
}