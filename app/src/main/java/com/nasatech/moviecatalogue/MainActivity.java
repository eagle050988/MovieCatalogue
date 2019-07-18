package com.nasatech.moviecatalogue;

import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nasatech.moviecatalogue.Alarm.AlarmReceiver;
import com.nasatech.moviecatalogue.activity.FavoriteActivity;
import com.nasatech.moviecatalogue.activity.SettingActivity;
import com.nasatech.moviecatalogue.adapter.MainFragmentPagerAdapter;
import com.nasatech.moviecatalogue.db.FavoriteHelper;
import com.nasatech.moviecatalogue.fragment.MovieList;
import com.nasatech.moviecatalogue.fragment.TvShowList;
import com.nasatech.moviecatalogue.widget.UpdateWidgetService;

public class MainActivity extends AppCompatActivity {
    static final int SETTING_SET_UP = 1;  // The request code
    private FavoriteHelper favoriteHelper;
    private ViewPager viewPager;
    private TabLayout.Tab ActiveTab;
    private TabLayout tabLayout;
    private AlarmReceiver alarmReceiver;
    private Boolean Daily, Release;

    private static int jobId = 1001;
    private static int SCHEDULE_OF_PERIOD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViews();

        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);

        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        if (savedInstanceState != null) {
            int activetab = savedInstanceState.getInt("activetab");

            ActiveTab = tabLayout.getTabAt(activetab);

        }

        alarmReceiver = new AlarmReceiver();

//        Daily = alarmReceiver.isWorking(this, AlarmReceiver.TYPE_DAILY);
//        Release = alarmReceiver.isWorking(this, AlarmReceiver.TYPE_RELEASE);

        ComponentName mServiceComponent = new ComponentName(this, UpdateWidgetService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, mServiceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    private void InitViews() {
        viewPager = findViewById(R.id.viewPager);
        SetUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tbCatalog);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ActiveTab = tab;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (ActiveTab == null)
            ActiveTab = tabLayout.getTabAt(0);
    }

    private void SetUpViewPager(ViewPager vp) {
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mainFragmentPagerAdapter.addFragment(new MovieList(), getString(R.string.movie));
        mainFragmentPagerAdapter.addFragment(new TvShowList(), getString(R.string.tv_show));
        vp.setAdapter(mainFragmentPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    PagerAdapter pagerAdapter = viewPager.getAdapter();

                    for (int i = 0; i < pagerAdapter.getCount(); i++) {
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);

                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (ActiveTab.getText() == getString(R.string.movie)) {
                                if (viewPagerFragment instanceof MovieList) {
                                    MovieList movieList = (MovieList) viewPagerFragment;
                                    if (movieList != null) {
                                        movieList.StartSearch(query);
                                        break;
                                    }
                                }
                            } else {
                                if (viewPagerFragment instanceof TvShowList) {
                                    TvShowList tvShowList = (TvShowList) viewPagerFragment;
                                    if (tvShowList != null) {
                                        tvShowList.StartSearch(query);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    PagerAdapter pagerAdapter = viewPager.getAdapter();

                    for (int i = 0; i < pagerAdapter.getCount(); i++) {
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);

                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (ActiveTab.getText() == getString(R.string.movie)) {
                                if (viewPagerFragment instanceof MovieList) {
                                    MovieList movieList = (MovieList) viewPagerFragment;
                                    if (movieList != null) {
                                        movieList.StartSearch(newText);
                                        break;
                                    }
                                }
                            } else {
                                if (viewPagerFragment instanceof TvShowList) {
                                    TvShowList tvShowList = (TvShowList) viewPagerFragment;
                                    if (tvShowList != null) {
                                        tvShowList.StartSearch(newText);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            });

            // be sure to use 'setOnQueryTextFocusChangeListener()'
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean newViewFocus) {
                    if (!newViewFocus) {
                        PagerAdapter pagerAdapter = viewPager.getAdapter();

                        for (int i = 0; i < pagerAdapter.getCount(); i++) {
                            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);

                            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                if (ActiveTab.getText() == getString(R.string.movie)) {
                                    if (viewPagerFragment instanceof MovieList) {
                                        MovieList movieList = (MovieList) viewPagerFragment;
                                        if (movieList != null) {
                                            movieList.ResetData();
                                            break;
                                        }
                                    }
                                } else {
                                    if (viewPagerFragment instanceof TvShowList) {
                                        TvShowList tvShowList = (TvShowList) viewPagerFragment;
                                        if (tvShowList != null) {
                                            tvShowList.ResetData();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
//        return super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("activetab", ActiveTab.getPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_favorite:
                Intent moveWithObjectIntent = new Intent(this, FavoriteActivity.class);
                //moveWithObjectIntent.putExtra(DetailMovie.EXTRA_MOVIE, movie);
                startActivity(moveWithObjectIntent);
                break;
            case R.id.action_setting:
                Intent Setting = new Intent(this, SettingActivity.class);
                startActivityForResult(Setting, SETTING_SET_UP);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTING_SET_UP) {
            SharedPreferences sharedPreferences = getSharedPreferences("com.nasatech.moviecatalogue.activity", MODE_PRIVATE);
            Release = sharedPreferences.getBoolean("Release", false);
            Daily = sharedPreferences.getBoolean("Daily", false);

            if (Release) {
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_RELEASE,
                        "08:00", "Release Alarm", AlarmReceiver.ID_RELEASE);
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_RELEASE);
            }

            if (Daily) {
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_DAILY,
                        "07:00", getString(R.string.release_message), AlarmReceiver.ID_DAILY);
            } else {
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_DAILY);
            }

        }
    }
}
