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
 * base class to encapsulate specific Database action from the Settings activity. The database actions
 * are potentially dangerous since all data can be lost if used by accident. If the user wants to
 * delete or import a database, he/she will need to go in the Settings screen and confirm the Clear
 * and Import actions.
 */
public abstract class DatabasePreference extends Preference {

    /**
     * the task to be executed
     */
    private AsyncTask<Void, Void, Void> _task = null;

    /**
     * default constructor
     * @param c the context
     * @param a the default values
     */
    public DatabasePreference(Context c, AttributeSet a) {
        super(c, a);
        _task = buildDatabaseTask();
    }

    /**
     * encapsulates the creation of the DatabaseTask that is intented to be executed once the
     * corresponding Preference item is clicked.
     * @return a DatabaseTask
     */
    protected abstract DatabaseTask buildDatabaseTask();

    /**
     * returns the ressource Id for the alert dialog title. If not used by the extending class, the
     * Alert dialog will not be shown to the user
     * @return A RessourceId to be used for the Alert Dialog title. Default value is -1, which means
     * no Alert Dialog will be shown.
     */
    protected int getTitleDialog() {
        // default implementation doesn't provide text, no dialog will be shown
        return -1;
    }

    /**
     * returns the ressource Id for the alert dialog message. If not used by the extending class, the
     * Alert dialog will not be shown to the user
     * @return A RessourceId to be used for the Alert Dialog message. Default value is -1, which means
     * no Alert Dialog will be shown.
     */
    protected int getMessageDialog() {
        // default implementation doesn't provide text, no dialog will be shown
        return -1;
    }

    /**
     * represent the action when the Preference Item is clicked. If the Ressource Id are set, it
     * will build an AlertDialog and show it to the user, otherwise it will execute the task
     * immediately.
     */
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

    /**
     * defensive programming to make sure we execute a task that has been set. If the task is null,
     * nothing happens.
     */
    private void executeTask() {
        if (_task != null) {
            _task.execute();
        }
    }

    /**
     * utility method to determines if the AlertDialog must be shown based on the RessourceId
     * retreived for the extending class.
     * @return true if Title Ress and Message Ress are different than -1, otherwise false
     */
    private boolean mustShowDialog() {
        return getMessageDialog() != -1 && getTitleDialog() != -1;
    }
}
