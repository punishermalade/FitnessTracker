package com.punisher.fitnesstracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.fragment.MutuallyExclusiveButtonsFragment;
import com.punisher.fitnesstracker.fragment.StatisticsFragment;
import com.punisher.fitnesstracker.stats.StatisticManager;
import com.punisher.fitnesstracker.task.StatisticCompilationTask;
import java.util.ArrayList;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity implements
        MutuallyExclusiveButtonsFragment.OnChangeActiveButtonListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar bar = (Toolbar)findViewById(R.id.toolbar_statistics);
        setSupportActionBar(bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "StatisticsActivity onResume()");

    }

    @Override
    public void onChangeActiveButton(final View v) {
        Log.i("fitness", "Button pressed: " + v.toString());

        AsyncTask<Void, Void, Void> task = new StatisticCompilationTask(this) {
            @Override
            protected List<FitnessActivity> getActivityList() {

                FitnessActivity.FitnessType fType = getFitnessTypeFromButtonId(v.getId());
                List<FitnessActivity> list = getDatabaseManager().getFitnessActivityList();
                List<FitnessActivity> filteredList = new ArrayList<>();

                for (FitnessActivity f : list) {
                    if (f.getFitnessType() == fType) {
                        filteredList.add(f);
                    }
                }

                return filteredList;
            }

            @Override
            protected void updateUI(StatisticManager manager) {
                manager.crunchStatistic(getActivityList());
                StatisticsFragment f = (StatisticsFragment)getFragmentManager().findFragmentById(R.id.stat_fragment);
                f.updateUIWithStat(manager);
            }
        }.execute();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.statistics_filter;
    }

    @Override
    public boolean mustActivateFirstButton() {
        return true;
    }

    private FitnessActivity.FitnessType getFitnessTypeFromButtonId(int i ) {
        if (i == R.id.stats_filter_swimming) {
            return FitnessActivity.FitnessType.SWIMMING;
        }
        else if (i == R.id.stats_filter_running) {
            return FitnessActivity.FitnessType.RUNNING;
        }
        else if (i == R.id.stats_filter_biking) {
            return FitnessActivity.FitnessType.BIKING;
        }

        return null;
    }
}
