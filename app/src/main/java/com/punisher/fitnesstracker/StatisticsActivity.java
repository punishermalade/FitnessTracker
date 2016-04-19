package com.punisher.fitnesstracker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.punisher.fitnesstracker.stats.StatisticManager;

public class StatisticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "StatisticsActivity onResume()");

    }

}
