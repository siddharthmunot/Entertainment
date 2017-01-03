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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.nutlabs.restaurant.R;
import com.nutlabs.restaurant.kore.utils.LogUtils;
import com.nutlabs.restaurant.kore.utils.TabsAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Container for the various music lists
 */
public class MovieListFragmentNew extends Fragment {
    private static final String TAG = LogUtils.makeLogTag(MovieListFragment.class);

    private TabsAdapter tabsAdapter;

    @InjectView(R.id.pager_tab_strip) PagerSlidingTabStrip pagerTabStrip;
    @InjectView(R.id.pager)
    ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_music_list, container, false);
        ButterKnife.inject(this, root);
        Bundle args=getArguments();
        //Bundle arg1=new Bundle();
        //arg1.putString(args.getString());
        tabsAdapter = new TabsAdapter(getActivity(), getChildFragmentManager())
                .addTab(MovieListFragmentFinal.class, args, R.string.all, 1)
                .addTab(VideoGenresListFragment.class, args, R.string.genres, 2);


        viewPager.setAdapter(tabsAdapter);

        pagerTabStrip.setViewPager(viewPager);
        return root;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
    }
}
