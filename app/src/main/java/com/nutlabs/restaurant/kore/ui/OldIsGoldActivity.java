/*
 * Copyright 2015 Synced Synapse. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nutlabs.restaurant.kore.ui;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.nutlabs.restaurant.R;
import com.nutlabs.restaurant.SharedConstants;
import com.nutlabs.restaurant.dlna.CustomListAdapter;
import com.nutlabs.restaurant.dlna.CustomListItem;
import com.nutlabs.restaurant.kore.utils.LogUtils;
import com.nutlabs.restaurant.kore.utils.Utils;

/**
 * Controls the presentation of Movies information (list, details)
 * All the information is presented by specific fragments
 */
public class OldIsGoldActivity extends BaseActivity
        implements MovieListFragmentFinal.OnMovieSelectedListener,VideoGenresListFragment.OnVideoGenreSelectedListener {
    private static final String TAG = LogUtils.makeLogTag(MoviesActivity.class);

    public static final String MOVIEID = "movie_id";
    public static final String MOVIETITLE = "movie_title";

    private int selectedMovieId = -1;
    private String selectedMovieTitle;

    private NavigationDrawerFragment navigationDrawerFragment;
    public ArrayAdapter<CustomListItem> mItemListAdapter;

    private int selectedGenreId = -1;
    private String selectedGenreTitle = null;

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request transitions on lollipop
        if (Utils.isLollipopOrLater()) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        mItemListAdapter = new CustomListAdapter(this);
        setContentView(R.layout.activity_generic_media);

        // Set up the drawer.
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if (savedInstanceState == null) {
            MovieListFragmentNew movieListFragment = new MovieListFragmentNew();
            Bundle args = new Bundle();
            args.putString(SharedConstants.MOVIE_TAG, SharedConstants.MOVIE_TAG_OLDISGOLD);
            movieListFragment.setArguments(args);

            // Setup animations
            if (Utils.isLollipopOrLater()) {
                movieListFragment.setExitTransition(null);
                movieListFragment.setReenterTransition(TransitionInflater
                        .from(this)
                        .inflateTransition(android.R.transition.fade));
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, movieListFragment)
                    .commit();
        } else {
            selectedMovieId = savedInstanceState.getInt(MOVIEID, -1);
            selectedMovieTitle = savedInstanceState.getString(MOVIETITLE, null);
        }

        setupActionBar(selectedMovieTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVIEID, selectedMovieId);
        outState.putString(MOVIETITLE, selectedMovieTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
//            getMenuInflater().inflate(R.menu.media_info, menu);
//        }
        getMenuInflater().inflate(R.menu.media_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_show_remote:
                // Starts remote
                Intent launchIntent = new Intent(this, RemoteActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(launchIntent);
                return true;*/
            case android.R.id.home:
                // Only respond to this if we are showing the movie details in portrait mode,
                // which can be checked by checking if selected movie != -1, in which case we
                // should go back to the previous fragment, which is the list.
                if (selectedMovieId != -1) {
                    selectedMovieId = -1;
                    selectedMovieTitle = null;
                    setupActionBar(null);
                    getSupportFragmentManager().popBackStack();
                    return true;
                }else if (selectedGenreId != -1) {
                    selectedGenreId = -1;
                    selectedGenreTitle = null;
                    setupActionBar(null);
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If we are showing movie details in portrait, clear selected and show action bar
        if (selectedMovieId != -1) {
            selectedMovieId = -1;
            selectedMovieTitle = null;
            setupActionBar(null);
        }
        super.onBackPressed();
    }

    private void setupActionBar(String movieTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (movieTitle != null) {
            navigationDrawerFragment.setDrawerIndicatorEnabled(false);
            actionBar.setTitle(movieTitle);
        } else {
            navigationDrawerFragment.setDrawerIndicatorEnabled(true);
            actionBar.setTitle(R.string.old_is_gold);
        }
    }

    /**
     * Callback from movielist fragment when a movie is selected.
     * Switch fragment in portrait
     *
     * @param movieId    Movie selected
     * @param movieTitle Title
     */
    @TargetApi(21)
    public void onMovieSelected(int movieId, String movieTitle) {
        selectedMovieId = movieId;
        selectedMovieTitle = movieTitle;

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

    public void onVideoGenreSelected(int genreId, String genreTitle) {
        selectedGenreId = genreId;
        selectedGenreTitle = genreTitle;


        // Replace list fragment
        MovieListFragmentFinal movieListFragment = MovieListFragmentFinal.newInstanceForGenre(genreId,genreTitle, SharedConstants.MOVIE_TAG_OLDISGOLD);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_details_enter, 0, R.anim.fragment_list_popenter, 0)
                .replace(R.id.fragment_container, movieListFragment)
                .addToBackStack(null)
                .commit();
        setupActionBar(genreTitle);
    }
}