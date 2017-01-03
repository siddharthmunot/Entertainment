package com.nutlabs.restaurant;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.nutlabs.restaurant.kore.ui.BaseActivity;
import com.nutlabs.restaurant.kore.ui.MovieDetailsFragment;
import com.nutlabs.restaurant.kore.ui.NavigationDrawerFragment;
import com.nutlabs.restaurant.kore.utils.Utils;

public class MainActivityNew extends BaseActivity  implements MovieFragment.OnMovieSelectedListener,FragmentManager.OnBackStackChangedListener{

    private int selectedMovieId = -1;
    private String selectedMovieTitle;
   // private NavigationDrawerFragment navigationDrawerFragment;
    private ImageView backgroundImageView;
    //private ViewFlipper backgroundImageView;

    public static SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String TAG = "Pinut-Activity_NEW";

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request transitions on lollipop
        if (Utils.isLollipopOrLater()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

       /* backgroundImageView = (ImageView) findViewById(R.id.imageEntertainmentBackground);
        backgroundImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityNew.this,NavigationDetailActivity.class);
                startActivity(intent);

            }
        });*/


        //backgroundImageView=(ViewFlipper)findViewById(R.id.viewflipper);

        // Set up the drawer.
      /*  navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));*/

        MainActivityFragments mainActivityFragment = new MainActivityFragments();
        //MovieFragment mainActivityFragment = new MovieFragment();

        // Setup animations
        if (Utils.isLollipopOrLater()) {
            mainActivityFragment.setExitTransition(null);
            mainActivityFragment.setReenterTransition(TransitionInflater
                    .from(this)
                    .inflateTransition(android.R.transition.fade));
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mainActivityFragment)
                .commit();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        setupActionBar(null);
        //backgroundImageView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        return true;
    }

    @Override
    public void onResume() {
     /*   Log.i(TAG, "Main Activity New resumed");
        Fragment f =  getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(f instanceof MovieDetailsFragment) ) {
            Log.i(TAG, "Moviesactivity fragment is NULL");
            backgroundImageView.setVisibility(View.VISIBLE);
            // add your code here
        }
        else {
            Log.i(TAG, "Moviesactivity fragment is NOT NULL");
        }*/
        //ViewFlipper backgroundImageView1 = (ViewFlipper) findViewById(R.id.viewflipper);
        //backgroundImageView1.setVisibility(View.VISIBLE);
        super.onResume();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (selectedMovieId != -1) {
            selectedMovieId = -1;
            selectedMovieTitle = null;
            setupActionBar(null);
//            backgroundImageView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStack();
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(21)
    public void onMovieSelected(int movieId, String movieTitle) {
        selectedMovieId = movieId;
        selectedMovieTitle = movieTitle;
//        backgroundImageView.setVisibility(View.GONE);
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(movieId);
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();

        // Set up transitions
        if (Utils.isLollipopOrLater()) {
            movieDetailsFragment.setEnterTransition(TransitionInflater
                    .from(this)
                    .inflateTransition(R.transition.media_details));
            movieDetailsFragment.setReturnTransition(null);
        } else {
            fragTrans.setCustomAnimations(R.anim.fragment_details_enter, 0,
                    R.anim.fragment_list_popenter, 0);
        }
        fragTrans.replace(R.id.fragment_container, movieDetailsFragment)
                .addToBackStack(null)
                .commit();
        setupActionBar(selectedMovieTitle);
    }

    private void setupActionBar(String movieTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        //actionBar.setDisplayHomeAsUpEnabled(true);
        if (movieTitle != null) {
            //navigationDrawerFragment.setDrawerIndicatorEnabled(false);
            actionBar.setTitle(movieTitle);
        } else {
           // navigationDrawerFragment.setDrawerIndicatorEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }

    }

    @Override
    public void onBackStackChanged() {
        //Log.i(TAG, "Back Stance Called");
        Toast.makeText(this, "ON Cback stack change", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed Called");
        Fragment myf =  getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(myf instanceof MovieDetailsFragment) ) {
            Log.i(TAG, "Moviesactivity fragment is NULL");
//            backgroundImageView.setVisibility(View.VISIBLE);
            setupActionBar(null);
            // add your code here
        }
        else {
            Log.i(TAG, "Moviesactivity fragment is NOT NULL");
        }

    }



}
