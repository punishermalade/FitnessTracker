package com.punisher.fitnesstracker.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.punisher.fitnesstracker.database.DatabaseManager;

/**
 * Created by punisher on 2016-04-12.
 */
public abstract class DatabaseTask extends AsyncTask<Void, Void, Void> {

    protected ProgressDialog progressDialog = null;
    protected Context mainContext = null;
    protected DatabaseManager dbManager = null;

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

    protected abstract void doTask();
    protected abstract void refreshUI();

    @Override
    protected Void doInBackground(Void... params) {
        doTask();
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        refreshUI();

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
