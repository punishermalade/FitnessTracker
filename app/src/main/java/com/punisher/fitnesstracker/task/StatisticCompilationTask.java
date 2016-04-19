package com.punisher.fitnesstracker.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.database.DatabaseManager;
import com.punisher.fitnesstracker.dto.FitnessActivity;
import com.punisher.fitnesstracker.stats.StatisticManager;

import java.util.List;

/**
 * encapsulate a task to compile statistics. It provides a ProgressDialog to show the user something
 * is happenning
 */
public abstract class StatisticCompilationTask extends AsyncTask<Void, Void, Void> {

    /**
     * the default StatisticManager, all calculation are made in this object
     */
    private StatisticManager _statManager = null;

    /**
     * default ProgressDialog that is shown to the user
     */
    private ProgressDialog _dialog = null;

    /**
     * default DatabaseManager to retreive the data
     */
    private DatabaseManager _dbManager = null;

    /**
     * default constructor  that needs the context to initialize the ProgressDialog and DatabaseManager
     * @param c the context
     */
    public StatisticCompilationTask(Context c) {
        _statManager = new StatisticManager();

        _dialog = new ProgressDialog(c);
        _dialog.setIndeterminate(true);
        _dialog.setMessage(c.getString(R.string.stat_loading_dialog_text));

        _dbManager = new DatabaseManager(c);
    }

    /**
     * return a list of activity. depending on what statistic is to be displayed, it varies
     * @return a list of FitnessActivity
     */
    protected abstract List<FitnessActivity> getActivityList();

    /**
     * update the UI with the compiled data from the StatisticManager
     * @param manager
     */
    protected abstract void updateUI(StatisticManager manager);

    @Override
    protected void onPreExecute() {
        _dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        _statManager.crunchStatistic(getActivityList());
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        updateUI(_statManager);
        _dialog.dismiss();
    }

    /**
     * return a reference to the DatabaseManager instance
     * @return a DatabaseManager instance
     */
    protected DatabaseManager getDatabaseManager() {
        return _dbManager;
    }
}
