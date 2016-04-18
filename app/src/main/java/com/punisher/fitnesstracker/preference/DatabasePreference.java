package com.punisher.fitnesstracker.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * Created by punisher on 2016-04-18.
 */
public abstract class DatabasePreference extends Preference {

    private AsyncTask<Void, Void, Void> _task = null;

    public DatabasePreference(Context c, AttributeSet a) {
        super(c, a);
        _task = buildDatabaseTask();
    }

    protected abstract DatabaseTask buildDatabaseTask();

    protected int getTitleDialog() {
        // default implementation doesn't provide text, no dialog will be shown
        return -1;
    }

    protected int getMessageDialog() {
        // default implementation doesn't provide text, no dialog will be shown
        return -1;
    }

    @Override
    public void onClick() {

        if (mustShowDialog()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getString(getTitleDialog()));
            builder.setCancelable(true);
            builder.setMessage(getContext().getString(getMessageDialog()));
            builder.setPositiveButton(getContext().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executeTask();
                }
            });
            builder.setNegativeButton(getContext().getString(android.R.string.no), null);
            builder.show();
        }
        else {
            executeTask();
        }
    }

    private void executeTask() {
        if (_task != null) {
            _task.execute();
        }
    }

    private boolean mustShowDialog() {
        return getMessageDialog() != -1 && getTitleDialog() != -1;
    }
}
