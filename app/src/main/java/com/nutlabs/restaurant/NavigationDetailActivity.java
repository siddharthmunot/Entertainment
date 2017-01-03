package com.nutlabs.restaurant;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.nutlabs.restaurant.kore.ui.BaseActivity;

public class NavigationDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_detail);
        setupActionBar();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Route");
    }
}
