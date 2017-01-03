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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nutlabs.restaurant.R;
import com.nutlabs.restaurant.SharedConstants;
import com.nutlabs.restaurant.kore.host.HostInfo;
import com.nutlabs.restaurant.kore.host.HostManager;
import com.nutlabs.restaurant.kore.jsonrpc.type.PlaylistType;
import com.nutlabs.restaurant.kore.provider.MediaContract;
import com.nutlabs.restaurant.kore.service.LibrarySyncService;
import com.nutlabs.restaurant.kore.utils.LogUtils;
import com.nutlabs.restaurant.kore.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Fragment that presents the album genres list
 */
public class VideoFolderListFragment extends AbstractListFragment {
    private static final String TAG = LogUtils.makeLogTag(VideoGenresListFragment.class);
    private String movieTag= SharedConstants.DEFAULT_MOVIE_TAG;
    private String baseTagString;
    private VideoGenresAdapter mAdapter;
    private ArrayList<String> lstCurrentFolder=new ArrayList<String>();
    public interface OnVideoGenreSelectedListener {
        public void onVideoGenreSelected(int genreId, String genreTitle, String baseTagString, boolean bIsLastLevel);
    }

    // Activity listener
    private OnVideoGenreSelectedListener listenerActivity;

    @Override
    protected String getListSyncType() { return LibrarySyncService.SYNC_ALL_MOVIES; }

    @Override
    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the movie id from the tag
                ViewHolder tag = (ViewHolder) view.getTag();

                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                String tagList = preferences.getString(SharedConstants.PREF_FOLDERSTRUCTURE,"");
                StringTokenizer multiTokenizer = new StringTokenizer(tagList, "#");
                String currToken;
                String currBaseTag=baseTagString+","+tag.genreTitle;
                boolean bIsLastLevel=false;
                while (multiTokenizer.hasMoreTokens()) {
                    //System.out.println(multiTokenizer.nextToken());
                    currToken = multiTokenizer.nextToken();
                    if (currToken.compareToIgnoreCase(currBaseTag)==0) {
                        bIsLastLevel=true;
                        break;
                    }
                }

                // Notify the activity

                listenerActivity.onVideoGenreSelected(tag.genreId, tag.genreTitle,currBaseTag,bIsLastLevel);
            }
        };
    }

    @Override
    protected CursorAdapter createAdapter() {
        mAdapter=new VideoGenresAdapter(getActivity());
        //return new VideoGenresAdapter(getActivity());
        return mAdapter;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id","genreid","title","thumbnail"});
        Set<String> uniqueEntry = new TreeSet<>(lstCurrentFolder);
        int i=0;
        for(String folder:uniqueEntry){
            matrixCursor.addRow(new Object[]{i+1,i+1, folder,""});
            i++;
        }

        //mAdapter.swapCursor(matrixCursor);
        mAdapter.changeCursor(matrixCursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected CursorLoader createCursorLoader() {
        HostInfo hostInfo = HostManager.getInstance(getActivity()).getHostInfo();
        //Uri uri = MediaContract.MovieGenres.buildMovieForGenreListUri()hostInfo != null ? hostInfo.getId() : -1);
        Uri uri = MediaContract.VideoGenres.buildVideoGenresListUri(hostInfo != null ? hostInfo.getId() : -1);

        StringBuilder selection = new StringBuilder();
        String selectionArgs[] = null;
        String searchFilter = getSearchFilter();


        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String genreList="";
        /*if(movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_SHORTMOVIES)==0){
            genreList=preferences.getString(SharedConstants.PREF_SHORTMOVIES,"");
        }else if(movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_ANIMATEDMOVIE)==0){
            genreList=preferences.getString(SharedConstants.PREF_ANIMATEDMOVIE,"");
        }else if(movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_TEDTALKS)==0){
            genreList=preferences.getString(SharedConstants.PREF_TEDTALKS,"");
        }else if(movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_OLDISGOLD)==0){
            genreList=preferences.getString(SharedConstants.PREF_OLDISGOLD,"");
        }else if((movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_COMPUTERSKILL)==0) || (movieTag.compareToIgnoreCase(SharedConstants.MOVIE_TAG_ENGLISHEDUCATION)==0)) {*/
            //read the preference strings for the

            lstCurrentFolder.clear();
            String tagList = preferences.getString(SharedConstants.PREF_FOLDERSTRUCTURE,"");
            StringTokenizer multiTokenizer = new StringTokenizer(tagList, "#");
            String currToken;
            String newToken;
            while (multiTokenizer.hasMoreTokens())
            {
                //System.out.println(multiTokenizer.nextToken());
                currToken=multiTokenizer.nextToken();
                if(currToken.contains(baseTagString)){
                    newToken=currToken.replace(baseTagString,"");
                    StringTokenizer currTokenizer = new StringTokenizer(newToken, ",");
                    if(currTokenizer.hasMoreTokens()){
                        lstCurrentFolder.add(currTokenizer.nextToken());
                    }
                }
            }
        //}

        List<String> items = Arrays.asList(genreList.split("\\s*,\\s*"));
        boolean firstVal=true;

        int cntr = 0;
        if (!TextUtils.isEmpty(searchFilter)) {
            /*selectionArgs = new String[items.size()+1];
            for (String tag : items) {
                if (firstVal == false) {
                    selection.append(" OR ");
                }
                selection.append(MediaContract.VideoGenres.TITLE + " LIKE ?");
                firstVal = false;
                selectionArgs[cntr++] = "%" + tag + "%";
            }
            if (selection.length() != 0)
                selection.append(" AND ");

            selection.append( MediaContract.VideoGenres.TITLE + " LIKE ?");
            selectionArgs[cntr++] = "%" + searchFilter + "%";
            */
            selectionArgs = new String[1];
            selection.append( MediaContract.VideoGenres.TITLE + " LIKE ?");
            selectionArgs[cntr++] = "%" + searchFilter + "%";
        }
        else{
            selectionArgs = new String[items.size()];
            for (String tag : items) {
                if (firstVal == false) {
                    selection.append(" OR ");
                }
                selection.append(MediaContract.VideoGenres.TITLE + " LIKE ?");
                firstVal = false;
                selectionArgs[cntr++] = "%" + tag + "%";
            }
        }
        //ArrayAdapter<String> adaper=new ArrayAdapter<String>(getActivity(),R.layout.fragment_music_list);

        //new CursorLoader()
        return new CursorLoader(getActivity(), uri,
                VideoGenreListQuery.PROJECTION, selection.toString(), selectionArgs, VideoGenreListQuery.SORT);

        /*HostInfo hostInfo = HostManager.getInstance(getActivity()).getHostInfo();
        Uri uri = MediaContract.AudioGenres.buildAudioGenresListUri(hostInfo != null ? hostInfo.getId() : -1);

        String selection = null;
        String selectionArgs[] = null;
        String searchFilter = getSearchFilter();
        if (!TextUtils.isEmpty(searchFilter)) {
            selection = MediaContract.AudioGenres.TITLE + " LIKE ?";
            selectionArgs = new String[] {"%" + searchFilter + "%"};
        }

        return new CursorLoader(getActivity(), uri,
                VideoGenreListQuery.PROJECTION, selection, selectionArgs, VideoGenreListQuery.SORT);

                */
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listenerActivity = (OnVideoGenreSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnVideoGenreSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenerActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            movieTag = getArguments().getString(SharedConstants.MOVIE_TAG, null);

            baseTagString=getArguments().getString(SharedConstants.BASE_TAG_STRING, null);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.media_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.action_search_genres));
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Audio genres list query parameters.
     */
    private interface VideoGenreListQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                MediaContract.VideoGenres.GENREID,
                MediaContract.VideoGenres.TITLE,
                MediaContract.VideoGenres.THUMBNAIL,
        };

        String SORT = MediaContract.VideoGenres.TITLE + " ASC";

        final int ID = 0;
        final int GENREID = 1;
        final int TITLE = 2;
        final int THUMBNAIL = 3;
    }

    private class VideoGenresAdapter extends CursorAdapter {

        private HostManager hostManager;
        private int artWidth, artHeight;


        public VideoGenresAdapter(Context context) {
            super(context, null, false);
            this.hostManager = HostManager.getInstance(context);

            // Get the art dimensions
            Resources resources = context.getResources();
            artWidth = (int)(resources.getDimension(R.dimen.audiogenrelist_art_width) /
                    UIUtils.IMAGE_RESIZE_FACTOR);
            artHeight = (int)(resources.getDimension(R.dimen.audiogenrelist_art_heigth) /
                    UIUtils.IMAGE_RESIZE_FACTOR);
        }



        /** {@inheritDoc} */
        @Override
        public View newView(Context context, final Cursor cursor, ViewGroup parent) {

            final View view = LayoutInflater.from(context)
                    .inflate(R.layout.grid_item_audio_genre, parent, false);

            // Setup View holder pattern
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView)view.findViewById(R.id.title);
            viewHolder.artView = (ImageView)view.findViewById(R.id.art);

            view.setTag(viewHolder);
            return view;
        }

        /** {@inheritDoc} */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder viewHolder = (ViewHolder)view.getTag();
            viewHolder.genreId = cursor.getInt(VideoGenreListQuery.GENREID);
            viewHolder.genreTitle = cursor.getString(VideoGenreListQuery.TITLE);

            viewHolder.titleView.setText(viewHolder.genreTitle);

            String thumbnail = cursor.getString(VideoGenreListQuery.THUMBNAIL);
            UIUtils.loadImageWithCharacterAvatar(context, hostManager,
                    thumbnail, viewHolder.genreTitle.toUpperCase(),
                    viewHolder.artView, artWidth, artHeight);

            // For the popupmenu
           /* ImageView contextMenu = (ImageView)view.findViewById(R.id.list_context_menu);
            contextMenu.setTag(viewHolder);
            contextMenu.setOnClickListener(genrelistItemMenuClickListener);*/
        }
    }

    /**
     * View holder pattern
     */
    private static class ViewHolder {
        TextView titleView;
        ImageView artView;

        int genreId;
        String genreTitle;
    }

    private View.OnClickListener genrelistItemMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final ViewHolder viewHolder = (ViewHolder)v.getTag();

            final PlaylistType.Item playListItem = new PlaylistType.Item();
            playListItem.genreid = viewHolder.genreId;

            final PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.getMenuInflater().inflate(R.menu.musiclist_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_play:
                            //MediaPlayerUtils.play(AudioGenresListFragment.this, playListItem);
                            return true;
                        case R.id.action_queue:
                            //MediaPlayerUtils.queueAudio(AudioGenresListFragment.this, playListItem);
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    };
}

