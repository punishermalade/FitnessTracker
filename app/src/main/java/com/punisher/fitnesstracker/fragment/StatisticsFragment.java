package com.punisher.fitnesstracker.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.stats.StatisticManager;
import com.punisher.fitnesstracker.util.FormatUtil;


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
    }

    /**
     * update the UI with the compiled stat
     * @param stat
     */
    public void updateUIWithStat(StatisticManager stat) {
        TextView tDist = (TextView)_view.findViewById(R.id.stat_total_distance);
        tDist.setText(FormatUtil.getDistance((int) stat.getValues(StatisticManager.TOTAL_DISTANCE)));

        TextView tDur = (TextView)_view.findViewById(R.id.stat_total_duration);
        tDur.setText(FormatUtil.formatDuration((int) stat.getValues(StatisticManager.TOTAL_DURATION)));

        TextView lDist = (TextView)_view.findViewById(R.id.stat_longest_distance);
        lDist.setText(FormatUtil.getDistance((int) stat.getValues(StatisticManager.LONGEST_DISTANCE)));

        TextView lDur = (TextView)_view.findViewById(R.id.stat_longest_duration);
        lDur.setText(FormatUtil.formatDuration((int)stat.getValues(StatisticManager.LONGEST_DURATION)));

        TextView bAvg = (TextView)_view.findViewById(R.id.stat_best_average);
        bAvg.setText(FormatUtil.formatAverage((int)stat.getValues(StatisticManager.BEST_AVERAGE)));

        TextView tAct = (TextView)_view.findViewById(R.id.stat_total_activity);
        tAct.setText(String.valueOf(stat.getValues(StatisticManager.TOTAL_ACTIVITY)));
    }

}
