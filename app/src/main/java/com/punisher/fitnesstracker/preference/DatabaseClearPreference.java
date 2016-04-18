package com.punisher.fitnesstracker.preference;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

import org.w3c.dom.Attr;

/**
 * Created by punisher on 2016-04-18.
 */
public class DatabaseClearPreference extends DatabasePreference {

    public DatabaseClearPreference(Context c, AttributeSet a) {
        super(c, a);
    }

    @Override
    protected DatabaseTask buildDatabaseTask() {
        return new DatabaseTask(getContext(), getContext().getString(R.string.progress_bar_clearing_msg)) {
            @Override
            protected void doTask() {
                dbManager.clearDatabase();
            }

            @Override
            protected void refreshUI() {
                // no UI to be refreshed here
            }
        };
    }

    @Override
    protected int getTitleDialog() {
        return R.string.action_clear_db;
    }

    @Override
    protected  int getMessageDialog() {
        return R.string.dialog_clear_db_confirmation;
    }

}
