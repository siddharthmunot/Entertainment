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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nutlabs.restaurant.AboutUsActivity;
import com.nutlabs.restaurant.FeedbackActivity;
import com.nutlabs.restaurant.R;
import com.nutlabs.restaurant.SharedConstants;
import com.nutlabs.restaurant.kore.Settings;
import com.nutlabs.restaurant.kore.host.HostInfo;
import com.nutlabs.restaurant.kore.host.HostManager;
import com.nutlabs.restaurant.kore.ui.hosts.HostManagerActivity;
import com.nutlabs.restaurant.kore.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import com.nutlabs.restaurant.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */

public class NavigationDrawerFragment extends Fragment {
    private static final String TAG = LogUtils.makeLogTag(NavigationDrawerFragment.class);

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private static final int ACTIVITY_HOSTS = 0,
    //ACTIVITY_REMOTE = 1,
    //ACTIVITY_MOVIES = 2,
    //ACTIVITY_TVSHOWS = 3,
    //ACTIVITY_MUSIC = 4,
    ACTIVITY_SHORT_MOVIES = 2,
            ACTIVITY_ANIMATED_MOVIES = 3,
            ACTIVITY_TED_TALKS = 4,
            ACTIVITY_OLD_IS_GOLD = 5,
    //ACTIVITY_PVR = 5,
    //ACTIVITY_FILES = 6,
    //ACTIVITY_ADDONS = 7,
    //ACTIVITY_SETTINGS = 8;
    ACTIVITY_FEEDBACK=9,
            ACTIVITY_VIRAL_VIDEOS=10,
            ACTIVITY_NEW_ADDITIONS=11,
            ACTIVITY_AMENITIES=12,
            ACTIVITY_ABOUT_US=13;

    // The current selected item id (based on the activity)
    private static int selectedItemId = -1;

    // Delay to close the drawer (ms)
    private static final int CLOSE_DELAY = 250;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer,
                container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerItem item = (DrawerItem)parent.getItemAtPosition(position);
                selectItem(item, position);
            }
        });

        Resources.Theme theme = getActivity().getTheme();
        TypedArray styledAttributes = theme.obtainStyledAttributes(new int[] {
                R.attr.iconHosts,
                R.attr.iconRemote,
                R.attr.iconMovies,
                //R.attr.iconTvShows,
                R.attr.iconAnimatedVideos,
                //R.attr.iconMusic,
                R.attr.iconTedTalks,
                //R.attr.iconPVR,
                R.attr.iconKids,
                R.attr.iconFiles,
                R.attr.iconAddons,
                R.attr.iconSettings,
                R.attr.iconFeedback,
                R.attr.iconViralVideos,
                R.attr.iconNewAddition,
              //  R.attr.iconAmenities,
                R.attr.iconAboutUs,
        });

        HostInfo hostInfo = HostManager.getInstance(getActivity()).getHostInfo();
        String hostName = (hostInfo != null) ? hostInfo.getName() : getString(R.string.xbmc_media_center);

        Set<String> shownItems = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getStringSet(Settings.KEY_PREF_NAV_DRAWER_ITEMS,
                        new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.entry_values_nav_drawer_items))));

        ArrayList<DrawerItem> items = new ArrayList<>(15);
        items.add(new DrawerItem(DrawerItem.TYPE_HOST, ACTIVITY_HOSTS, hostName,
                styledAttributes.getResourceId(ACTIVITY_HOSTS, 0)));
        //items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_REMOTE,
        //                       getString(R.string.remote),
        //                     styledAttributes.getResourceId(ACTIVITY_REMOTE, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_SHORT_MOVIES)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_SHORT_MOVIES,
                    SharedConstants.ACTIVITY_NAME_SHORTMOVIES,
                    styledAttributes.getResourceId(ACTIVITY_SHORT_MOVIES, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_ANIMATED_MOVIES)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ANIMATED_MOVIES,
                    SharedConstants.ACTIVITY_NAME_ANIMATEDMOVIE,
                    styledAttributes.getResourceId(ACTIVITY_ANIMATED_MOVIES, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_TED_TALKS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_TED_TALKS,
                    SharedConstants.ACTIVITY_NAME_TEDTALKS ,
                    styledAttributes.getResourceId(ACTIVITY_TED_TALKS, 0)));
        if (shownItems.contains(String.valueOf(ACTIVITY_OLD_IS_GOLD)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_OLD_IS_GOLD,
                    SharedConstants.ACTIVITY_NAME_OLDISGOLD ,
                    styledAttributes.getResourceId(ACTIVITY_OLD_IS_GOLD, 0)));
        /*if (shownItems.contains(String.valueOf(ACTIVITY_PVR)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_PVR,
                                     getString(R.string.tv_radio),
                                     styledAttributes.getResourceId(ACTIVITY_PVR, 0))); */
       /* if (shownItems.contains(String.valueOf(ACTIVITY_FILES)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_FILES,
                                     getString(R.string.files),
                                     styledAttributes.getResourceId(ACTIVITY_FILES, 0)));*
        /*if (shownItems.contains(String.valueOf(ACTIVITY_ADDONS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ADDONS,
                                     getString(R.string.addons),
                                     styledAttributes.getResourceId(ACTIVITY_ADDONS, 0)));*/
        // items.add(new DrawerItem()); // Divider
        //items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_SETTINGS,
        //                        getString(R.string.settings),
        //                        styledAttributes.getResourceId(ACTIVITY_SETTINGS, 0)));

        if (shownItems.contains(String.valueOf(ACTIVITY_FEEDBACK)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_FEEDBACK,
                    getString(R.string.feedback),
                    styledAttributes.getResourceId(ACTIVITY_FEEDBACK, 0)));

        if (shownItems.contains(String.valueOf(ACTIVITY_VIRAL_VIDEOS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_VIRAL_VIDEOS,
                    getString(R.string.viral_videos),
                    styledAttributes.getResourceId(ACTIVITY_VIRAL_VIDEOS, 0)));


        if (shownItems.contains(String.valueOf(ACTIVITY_NEW_ADDITIONS)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_NEW_ADDITIONS,
                    getString(R.string.newAddition),
                    styledAttributes.getResourceId(ACTIVITY_NEW_ADDITIONS, 0)));

        /*if (shownItems.contains(String.valueOf(ACTIVITY_AMENITIES)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_AMENITIES,
                    getString(R.string.amenities),
                    styledAttributes.getResourceId(ACTIVITY_AMENITIES, 0)));*/

        if (shownItems.contains(String.valueOf(ACTIVITY_ABOUT_US)))
            items.add(new DrawerItem(DrawerItem.TYPE_NORMAL_ITEM, ACTIVITY_ABOUT_US,
                    getString(R.string.aboutus),
                    styledAttributes.getResourceId(ACTIVITY_ABOUT_US, 0)));



        styledAttributes.recycle();
        mDrawerListView.setAdapter(new DrawerItemAdapter(
                getActivity(),
                R.layout.list_item_navigation_drawer,
                items.toArray(new DrawerItem[items.size()])));

        return mDrawerListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedItemId = getItemIdFromActivity();
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                saveUserLearnedDrawer();
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                saveUserLearnedDrawer();
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        selectedItemId = getItemIdFromActivity();
    }

    /**
     * Auxiliary method to show/hide the drawer indicator
     * @param isEnabled Show/hide enable drawer indicator
     */
    public void setDrawerIndicatorEnabled(boolean isEnabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(isEnabled);
    }

    public boolean isDrawerIndicatorEnabled() {
        return mDrawerToggle.isDrawerIndicatorEnabled();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    private void saveUserLearnedDrawer() {
        if (!mUserLearnedDrawer) {
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }
    }

    /**
     * Maps from the current activity to the private Item Id on the drawer
     * @return Item if of the current activity
     */
    private int getItemIdFromActivity() {
        Activity activity = getActivity();

        if (activity instanceof HostManagerActivity)
            return ACTIVITY_HOSTS;
        // else if (activity instanceof RemoteActivity)
        //    return ACTIVITY_REMOTE;
        if (activity instanceof ShortMoviesActivity)
            return ACTIVITY_SHORT_MOVIES;
        else if (activity instanceof AnimatedMoviesActivity)
            return ACTIVITY_ANIMATED_MOVIES;
        else if (activity instanceof TedTalksActivity)
            return ACTIVITY_TED_TALKS;
        else if (activity instanceof OldIsGoldActivity)
            return ACTIVITY_OLD_IS_GOLD;
            //else if (activity instanceof PVRActivity)
            //   return ACTIVITY_PVR;
            // else if (activity instanceof FileActivity)
            //    return ACTIVITY_FILES;
            //else if (activity instanceof AddonsActivity)
            //  return ACTIVITY_ADDONS;
            //else if (activity instanceof SettingsActivity)
            //  return ACTIVITY_SETTINGS;
        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
        else if (activity instanceof FeedbackActivity)
            return ACTIVITY_FEEDBACK;
     /*   else if (activity instanceof AmenitiesActivity)
            return ACTIVITY_AMENITIES;*/
        else if (activity instanceof AboutUsActivity)
            return ACTIVITY_ABOUT_US;


        return -1;
    }

    /**
     * Map from the Item Id to the activities
     */
    private static final SparseArray<Class> activityItemIdMap;
    static {
        activityItemIdMap = new SparseArray<>(12);
        activityItemIdMap.put(ACTIVITY_HOSTS, HostManagerActivity.class);
        //activityItemIdMap.put(ACTIVITY_REMOTE, RemoteActivity.class);
        activityItemIdMap.put(ACTIVITY_SHORT_MOVIES, ShortMoviesActivity.class);
        activityItemIdMap.put(ACTIVITY_ANIMATED_MOVIES, AnimatedMoviesActivity.class);
        activityItemIdMap.put(ACTIVITY_TED_TALKS, TedTalksActivity.class);
        activityItemIdMap.put(ACTIVITY_OLD_IS_GOLD, OldIsGoldActivity.class);
        //activityItemIdMap.put(ACTIVITY_TVSHOWS, TVShowsActivity.class);
        //activityItemIdMap.put(ACTIVITY_PVR, PVRActivity.class);
        //activityItemIdMap.put(ACTIVITY_ADDONS, AddonsActivity.class);
        //activityItemIdMap.put(ACTIVITY_SETTINGS, SettingsActivity.class);
        activityItemIdMap.put(ACTIVITY_FEEDBACK, FeedbackActivity.class);
    /*    activityItemIdMap.put(ACTIVITY_AMENITIES, AmenitiesActivity.class);*/
        activityItemIdMap.put(ACTIVITY_ABOUT_US, AboutUsActivity.class);

    }
    private void selectItem(DrawerItem item, int position) {
        if (item.type == DrawerItem.TYPE_DIVIDER) return;

        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        // Same activity, just return
        if (item.id == getItemIdFromActivity())
            return;

        //no need to show hostmanager activity to user
        if (item.id==0)
            return;

        final Intent launchIntentFinal = new Intent(getActivity(),
                activityItemIdMap.get(item.id))
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(launchIntentFinal);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }, CLOSE_DELAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }

    /**
     * Auxiliary class to hold the description and icon of a drawer icon
     */
    public static class DrawerItem {
        public static final int TYPE_HOST = 0,
                TYPE_DIVIDER = 1,
                TYPE_NORMAL_ITEM = 2;
        public static final int DEFAULT_DIVIDER_ID = -1;

        public final int id;
        public final int type;
        public final String desc;
        public final int iconResourceId;

        /**
         * Creates a standard drawer item
         * @param desc Name of the item
         * @param icon Icon to show
         */
        public DrawerItem(int type, int id, String desc, int icon) {
            this.type = type;
            this.id = id;
            this.desc = desc;
            this.iconResourceId = icon;
        }

        /**
         * Creates a divider drawer item
         */
        public DrawerItem() {
            this.id = DEFAULT_DIVIDER_ID;
            this.type = TYPE_DIVIDER;
            this.desc = null;
            this.iconResourceId = 0;
        }
    }

    public static class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

        private int selectedItemColor, hostItemColor;

        public DrawerItemAdapter(Context context, int layoutId, DrawerItem[] objects) {
            super(context, layoutId, objects);
            TypedArray styledAttributes = context
                    .getTheme()
                    .obtainStyledAttributes(new int[] {
                            R.attr.colorAccent,
                            R.attr.textColorOverPrimary
                    });
            Resources resources = context.getResources();
            selectedItemColor = styledAttributes.getColor(0, resources.getColor(R.color.accent_default));
            hostItemColor = styledAttributes.getColor(1, resources.getColor(R.color.dark_blue));
            styledAttributes.recycle();
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DrawerItem item = getItem(position);

            ImageView icon;
            TextView desc;
            ImageView image;
            switch (item.type) {
                case DrawerItem.TYPE_DIVIDER:
                    if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer_divider, parent, false);
                    }
                    break;
                case DrawerItem.TYPE_NORMAL_ITEM:
                    if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer, parent, false);
                    }
                    icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon);
                    icon.setImageResource(item.iconResourceId);
                    desc = (TextView)convertView.findViewById(R.id.drawer_item_title);
                    desc.setText(item.desc);
                    if (selectedItemId == item.id) {
                        icon.setColorFilter(selectedItemColor);
                        desc.setTextColor(selectedItemColor);
                    }
                    break;
                case DrawerItem.TYPE_HOST:
                    if (convertView == null) {
                        convertView = ((LayoutInflater)getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.list_item_navigation_drawer_host, parent, false);
                    }
                    //icon = (ImageView)convertView.findViewById(R.id.drawer_item_icon);
                    image = (ImageView)convertView.findViewById(R.id.drawer_image);
                    //icon.setImageResource(item.iconResourceId);
                    desc = (TextView)convertView.findViewById(R.id.drawer_item_title);
                    String name = PreferenceManager.getDefaultSharedPreferences(this.getContext()).getString(SharedConstants.PREF_NAME,"");
                    String[] names = name.split(" ");

                    String firstName = names[0].substring(0,1).toUpperCase() + names[0].substring(1).toLowerCase();
                    String navIntro = "Hi " + firstName;
                    Log.i(TAG, "name=" + firstName);
                    Log.i(TAG, "name" + navIntro);
                    desc.setText(navIntro);
                    if (selectedItemId == item.id) {
                        //icon.setColorFilter(selectedItemColor);
                        //desc.setTextColor(selectedItemColor);
                        image.setImageResource(R.drawable.images_entertainment);
                    } else {
                        //icon.setColorFilter(selectedItemColor);
                        //desc.setTextColor(selectedItemColor);
                        switch (selectedItemId) {
                            case 1:
                                image.setImageResource(R.drawable.navigation_remote);
                                break;
                            //SHORT Movie
                            case 2:
                                image.setImageResource(R.drawable.navigation_movies);
                                break;
                            //Animated Movie
                            case 3:
                                image.setImageResource(R.drawable.navigation_animation);
                                break;
                            //TED Talks
                            case 4:
                                image.setImageResource(R.drawable.navigation_ted);
                                break;
                            //Old is Gold
                            case 5:
                                image.setImageResource(R.drawable.navigation_kids);
                                break;
                            //Feedback
                            case 9:
                                image.setImageResource(R.drawable.navigation_feedback);
                                break;
                            //Amenities
                            case 10:
                                image.setImageResource(R.drawable.navigation_aboutus);
                                break;
                            //About Us
                            case 11:
                                image.setImageResource(R.drawable.navigation_aboutus);
                                break;
                        }

                    }
                    break;
            }
            return convertView;
        }
    }
}