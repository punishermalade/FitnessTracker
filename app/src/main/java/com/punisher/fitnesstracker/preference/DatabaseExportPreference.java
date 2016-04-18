package com.punisher.fitnesstracker.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * Created by punisher on 2016-04-18.
 */
public class DatabaseExportPreference extends DatabasePreference {

    public DatabaseExportPreference(Context c, AttributeSet attr) {
        super(c, attr);
    }

    @Override
    protected DatabaseTask buildDatabaseTask() {
        return new DatabaseTask(getContext(), getContext().getString(R.string.progress_bar_exporting_msg)) {
            @Override
            protected void doTask() {
                dbManager.exportDatabaseToStorage();
            }

            @Override
            protected void refreshUI() {
                // no UI to update!
            }
        };
    }
}
