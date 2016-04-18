package com.punisher.fitnesstracker.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;
import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.task.DatabaseTask;

/**
 * represents the Import Database preferences item. It creates the DatabaseTask to import the most
 * recent database backup.
 */
public class DatabaseImportPreference extends DatabasePreference {

    /**
     * represents the default constructor
     * @param c the context
     * @param a the default values
     */
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
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_import_db_completed), Toast.LENGTH_LONG).show();
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
