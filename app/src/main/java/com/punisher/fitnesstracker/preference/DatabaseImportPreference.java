package com.punisher.fitnesstracker.preference;

import android.app.Fragment;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * Created by punisher on 2016-04-18.
 */
public class DatabaseImportPreference extends DatabasePreference {

    public DatabaseImportPreference(Context c, AttributeSet a) {
        super(c, a);
    }

    @Override
    protected DatabaseTask buildDatabaseTask() {
        return new DatabaseTask(getContext(), getContext().getString(R.string.progress_bar_loading_msg)) {

            @Override
            protected void doTask() {
                dbManager.importDatabaseFromStorage();
            }

            @Override
            protected void refreshUI() {
                // no UI to be refreshed here
            }
        };
    }

    @Override
    protected int getTitleDialog() {
        return R.string.action_import_db;
    }

    @Override
    protected  int getMessageDialog() {
        return R.string.dialog_import_db_confirmation;
    }
}
