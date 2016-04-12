package com.punisher.fitnesstracker.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.punisher.fitnesstracker.database.DatabaseManager;

/**
 * Encapsulate an AsyncTask to use the DatabaseManager. Provides two handles to call any DatabaseManager
 * functions and to refresh the UI once the task is completed. This class is mainly to avoid code
 * duplication with the ProgressDialog.
 */
public abstract class DatabaseTask extends AsyncTask<Void, Void, Void> {

    /**
     * the Progress Dialog that is shown for each operation
     */
    protected ProgressDialog progressDialog = null;

    /**
     * the Context, needed for various stuff
     */
    protected Context mainContext = null;

    /**
     * the DatabaseManager instance, initialized in the constructor
     */
    protected DatabaseManager dbManager = null;

    /**
     * default constructor
     * @param c the Context
     * @param msg The message to be shown in the ProgressDialog
     */
    public DatabaseTask(Context c, String msg) {
        // build the dialog
        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        // keep the context
        mainContext = c;

        // get an instance of the databasemanager
        dbManager = new DatabaseManager(c);
    }

    /**
     * This handler is where the Database operation should be done. This is called from the AsyncTask
     * method doInBackground(...)
     */
    protected abstract void doTask();

    /**
     * This handler is where the UI can be refresh. This function is called first in the AsyncTask
     * method onPostExecute()
     */
    protected abstract void refreshUI();

    /**
     * call the doTask() function
     * @param params none is used
     * @return null
     */
    @Override
    protected Void doInBackground(Void... params) {
        doTask();
        return null;
    }

    /**
     * Just show the progress dialog
     */
    @Override
    protected void onPreExecute() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    /**
     * This handles the refreshUI function call and then dismiss the progress dialog.
     * @param v the result from the task
     */
    @Override
    protected void onPostExecute(Void v) {
        refreshUI();

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
