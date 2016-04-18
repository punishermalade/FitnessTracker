package com.punisher.fitnesstracker.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;
import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * represents the Clear Database preference item. It creates the DatabaseTask to clear the database
 * for the parent class.
 */
public class DatabaseClearPreference extends DatabasePreference {

    /**
     * represents the default constructor
     * @param c the context
     * @param a the default values
     */
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
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_clear_db_completed), Toast.LENGTH_LONG).show();
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
