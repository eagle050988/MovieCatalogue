package com.nasatech.moviecatalogue.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.adapter.MainFragmentPagerAdapter;
import com.nasatech.moviecatalogue.fragment.FavoriteMovie;
import com.nasatech.moviecatalogue.fragment.FavoriteTVShow;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        InitViews();
    }

    private void InitViews() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        SetUpViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tbCatalog);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void SetUpViewPager(ViewPager vp) {
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mainFragmentPagerAdapter.addFragment(new FavoriteMovie(), getString(R.string.movie));
        mainFragmentPagerAdapter.addFragment(new FavoriteTVShow(), getString(R.string.tv_show));
        vp.setAdapter(mainFragmentPagerAdapter);

    }
}
