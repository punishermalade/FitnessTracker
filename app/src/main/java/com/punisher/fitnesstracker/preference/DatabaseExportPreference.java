package com.punisher.fitnesstracker.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * represent the Database Export preference item. It creates the DatabaseTask for the parent class.
 */
public class DatabaseExportPreference extends DatabasePreference {

    /**
     * default constructor for the preference
     * @param c the context
     * @param attr the default values
     */
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
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_export_db_completed), Toast.LENGTH_LONG).show();
            }
        };
    }
}
