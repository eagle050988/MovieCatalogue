package com.nasatech.moviecatalogue.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.nasatech.moviecatalogue.R;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch swRelease;
    private Switch swDaily;

    private Boolean Daily, Release;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_schedule);

        swRelease = findViewById(R.id.sw_Release_Reminder);
        swDaily = findViewById(R.id.sw_daily_reminder);

        SharedPreferences sharedPreferences = getSharedPreferences("com.nasatech.moviecatalogue.activity", MODE_PRIVATE);
        swRelease.setChecked(sharedPreferences.getBoolean("Release", false));
        swDaily.setChecked(sharedPreferences.getBoolean("Daily", false));

        swDaily.setOnCheckedChangeListener(this);
        swRelease.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences("com.nasatech.moviecatalogue.activity", MODE_PRIVATE).edit();

        switch (buttonView.getId()) {
            case R.id.sw_Release_Reminder:
                Release = swRelease.isChecked();
                editor.putBoolean("Release", Release);
                editor.commit();
                break;
            case R.id.sw_daily_reminder:
                Daily = swDaily.isChecked();
                editor.putBoolean("Daily", Daily);
                editor.commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("Release", Release);
        outState.putBoolean("Daily", Daily);
        super.onSaveInstanceState(outState);
    }
}
