package com.punisher.fitnesstracker.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.database.DatabaseManager;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.stats.StatisticManager;
import com.punisher.fitnesstracker.task.StatisticCompilationTask;

import java.util.List;

/**
 * encapsulate the StatisticsFragment behavior
 */
public class StatisticsFragment extends Fragment {

    /**
     * the inflated View
     */
    private View _view = null;

    /**
     * default constructor
     */
    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.i("fitness", "StatisticFragment onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("fitness", "StatisticsFragment onCreateView");
        _view = inflater.inflate(R.layout.fragment_statistics, container, false);
        return _view;
}

    @Override
    public void onResume() {
        super.onResume();
        Log.i("fitness", "StatisticsFragment OnResume");

        AsyncTask<Void, Void, Void> task = new StatisticCompilationTask(getActivity()) {
            @Override
            protected List<FitnessActivity> getActivityList() {
                return getDatabaseManager().getFitnessActivityList();
            }

            @Override
            protected void updateUI(StatisticManager manager) {
                updateUIWithStat(manager);
            }
        }.execute();

    }

    /**
     * update the UI with the compiled stat
     * @param stat
     */
    private void updateUIWithStat(StatisticManager stat) {
        TextView v = (TextView)_view.findViewById(R.id.stat_longest_duration);
        v.setText(String.valueOf(stat.getValues(StatisticManager.LONGEST_DURATION)));
    }

}
