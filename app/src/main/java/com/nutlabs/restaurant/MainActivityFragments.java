package com.nutlabs.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nutlabs.restaurant.kore.ui.AnimatedMoviesActivity;
import com.nutlabs.restaurant.kore.ui.ShortMoviesActivity;
import com.nutlabs.restaurant.kore.ui.TedTalksActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by mdimran on 12/26/2015.
 */
public class MainActivityFragments extends Fragment {
    //private TabsAdapter tabsAdapter;

    //@InjectView(R.id.pager_tab_strip) PagerSlidingTabStrip pagerTabStrip;
    //@InjectView(R.id.pager) ViewPager viewPager;
    @InjectView(R.id.entertainment_button_list)LinearLayout viewEntertainmentButtonList;
    @InjectView(R.id.imageButtonShortMovies)Button btnShortMovies;
    @InjectView(R.id.imageButtonAnimatedMovies)Button btnAnimatedMovies;
    @InjectView(R.id.imageButtonTedTalks)Button btnTedTalks;

    //@InjectView(R.id.imageEntertainmentBackground)ImageView backgroundImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.inject(this, root);
        //final View viewEntertainmentButtonList=this.getView().findViewById(R.id.entertainment_button_list);
/*
        tabsAdapter = new TabsAdapter(getActivity(), getChildFragmentManager())
              .addTab(MovieFragment.class, getArguments(), R.string.tabEntertainment, 1)
            .addTab(FoodFragment.class, getArguments(), R.string.tabFood , 2)
          .addTab(FeedbackFragment.class, getArguments(), R.string.tabFeedback, 3);

        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    viewEntertainmentButtonList.setVisibility(View.GONE);
                }
                else
                {
                    viewEntertainmentButtonList.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

*/
        //getFragmentManager().beginTransaction().replace(this.getId(), new MovieFragment()).commit();
        //getFragmentManager().executePendingTransactions();

        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, new MovieFragment()).commit();
        getChildFragmentManager().executePendingTransactions();


        btnShortMovies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShortMoviesActivity.class);
                startActivity(intent);

            }
        });

        btnAnimatedMovies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnimatedMoviesActivity.class);
                startActivity(intent);

            }
        });

        btnTedTalks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TedTalksActivity.class);
                startActivity(intent);

            }
        });

        //pagerTabStrip.setViewPager(viewPager);
        return root;
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //backgroundImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart()
    {
        super.onStart ();
        //backgroundImageView.setVisibility(View.VISIBLE);
    }


}

