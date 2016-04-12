package com.punisher.fitnesstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.punisher.fitnesstracker.dto.FitnessActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * encapsulate the logic to manage the database
 */
public class DatabaseManager {

    private FitnessDBHelper _dbHelper = null;
    private Context _context = null;
    private boolean _hasChanged = false;


    /**
     * default constructor
     * @param c the Context
     */
    public DatabaseManager(Context c) {
        _dbHelper = new FitnessDBHelper(c);
        _context = c;
    }

    /**
     * this boolean indicated if a change in the database has been record since the last getFitnessActivityList()
     * call. Note: getFitnessActivityList() will read the database and return the latest information, no matter the
     * value returned by this function.
     * @return true if the database changed since last retreival, otherwise false.
     */
    public boolean hasChanged() {
        return _hasChanged;
    }

    public void insertNewFitnessActivity(FitnessActivity a) {

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FitnessDBContract.FitnessEntry.COLUMN_NAME_DAY, a.getDayOfActivity().getTime());
        values.put(FitnessDBContract.FitnessEntry.COLUMN_NAME_TIME, a.getTimeOfActivity().getTime());
        values.put(FitnessDBContract.FitnessEntry.COLUMN_NAME_FITNESS_TYPE, a.getFitnessType().toString());
        values.put(FitnessDBContract.FitnessEntry.COLUMN_NAME_DURATION, a.getDuration());
        values.put(FitnessDBContract.FitnessEntry.COLUMN_NAME_DISTANCE, a.getDistance());

        db.insert(FitnessDBContract.FitnessEntry.TABLE_NAME,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_NULLABLE,
                values);

        db.close();
        _hasChanged = true;

        Log.i("Fitness", "new activity added: " + a + " hasChanged:" + _hasChanged);
    }

    public void deleteActivity(FitnessActivity a) {

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        db.delete(FitnessDBContract.FitnessEntry.TABLE_NAME,
                FitnessDBContract.FitnessEntry._ID + "=" + a.getID(),
                null);

        db.close();
        _hasChanged = true;

        Log.i("fitness", "activity deleted: " + a.toString() + " hasChanged: " + _hasChanged);
    }

    public List<FitnessActivity> getFitnessActivityList() {

        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        String[] projection = new String[] {
                FitnessDBContract.FitnessEntry._ID,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_DAY,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_TIME,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_FITNESS_TYPE,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_DURATION,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_DISTANCE
        };

        Cursor c = db.query(
                FitnessDBContract.FitnessEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                FitnessDBContract.FitnessEntry.COLUMN_NAME_DAY + " DESC"
        );

        List<FitnessActivity> list = new ArrayList<FitnessActivity>();
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {

            FitnessActivity act = new FitnessActivity();
            act.setID(c.getString(c.getColumnIndex(FitnessDBContract.FitnessEntry._ID)));
            act.setDayOfActivity(new Date(c.getLong(c.getColumnIndex(FitnessDBContract.FitnessEntry.COLUMN_NAME_DAY))));
            act.setTimeOfActivity(new Date(c.getLong(c.getColumnIndex(FitnessDBContract.FitnessEntry.COLUMN_NAME_TIME))));
            act.setFitnessType(FitnessActivity.FitnessType.valueOf(
                    c.getString(c.getColumnIndex(FitnessDBContract.FitnessEntry.COLUMN_NAME_FITNESS_TYPE))));
            act.setDuration(c.getInt(c.getColumnIndex(FitnessDBContract.FitnessEntry.COLUMN_NAME_DURATION)));
            act.setDistance(c.getInt(c.getColumnIndex(FitnessDBContract.FitnessEntry.COLUMN_NAME_DISTANCE)));

            list.add(act);
            c.moveToNext();

            Log.i("Fitness", "new activity found: " + act);
        }

        c.close();
        db.close();

        _hasChanged = false;

        return list;

    }

    public boolean exportDatabaseToStorage() {

        Log.i("fitness", "Ext storage readable: " + isExternalStorageReadable() + " writable: " + isExternalStorageWritable());

        if (!checkPermissions()) {
            return false;
        }

        File file = new File(Environment.getExternalStorageDirectory().getPath() +
                             System.getProperty("file.separator") + FitnessDBHelper.DATABASE_NAME);

        Log.i("Fitness", "Existing DB path: " + file.getAbsolutePath());

        if (!file.mkdir()) {
            Log.i("fitness", "DB Path mkdir command returned false. Either WRITE permission is not enabled or directory already exists");
        }

        File dbFile = _context.getDatabasePath(FitnessDBHelper.DATABASE_NAME);

        String pathToSave = file.getAbsolutePath() + System.getProperty("file.separator") +
                            System.currentTimeMillis() + "_" + FitnessDBHelper.DATABASE_NAME;

        Log.i("fitness", "Saving db file : " + dbFile.getAbsolutePath() + System.getProperty("file.separator") +
                dbFile.getName() + " to " + pathToSave);

        try {
            FileOutputStream stream = new FileOutputStream(new File(pathToSave));
            FileInputStream inStream = new FileInputStream(dbFile);

            int read = 0;
            while ((read = inStream.read()) != -1) {
                stream.write(read);
            }

            stream.flush();
            stream.close();
            inStream.close();
        }
        catch (Exception ex) {
            Log.w("fitness", "copying the database to external storage failed", ex);
            return false;
        }

        return true;
    }

    public boolean importDatabaseFromStorage() {

        if (!checkPermissions()) {
            return false;
        }

        File savedDBDir = new File(Environment.getExternalStorageDirectory().getPath() +
                System.getProperty("file.separator") + FitnessDBHelper.DATABASE_NAME);

        // finding the latest backup
        File savedDB = getLatestBackup(savedDBDir);

        if (savedDB == null) {
            return false;
        }

        Log.i("fitness", "Backup DB Found: " + savedDB.getName());

        // get the database path for this application
        File appDBDir = _context.getDatabasePath(FitnessDBHelper.DATABASE_NAME);
        Log.i("fitness", "Application database found: " + appDBDir.getPath());

        try {

            FileOutputStream out = new FileOutputStream(appDBDir);
            FileInputStream in = new FileInputStream(savedDB);
            int read = 0;

            while (((read = in.read()) != -1)) {
                out.write(read);
            }

            out.flush();
            out.close();
            in.close();

        }
        catch (Exception ex) {
            Log.w("fitness", "Could not import the database", ex);
            return false;
        }

        return true;

    }

    public boolean clearDatabase() {

        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        db.delete(FitnessDBContract.FitnessEntry.TABLE_NAME, null, null);
        db.close();
        _hasChanged = true;

        return true;
    }

    private File getLatestBackup(File dir) {

        File last = null;

        try {

            File[] list = dir.listFiles();

            if (list.length > 0 && list[0].getName().contains(".db")) {
                last = list[0];

                for (int i  = 1; i < list.length; i++) {

                    File searched = list[i];
                    if ((searched.lastModified() > last.lastModified()) && (searched.getName().contains(".db"))) {
                        last = searched;
                    }

                }
            }
        }
        catch (Exception ex) {
            Log.w("fitness", "could not get the latest backup database", ex);
        }

        return last;
    }
    private boolean checkPermissions() {
        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Log.w("fitness", "WRITE or READ permission not set");
            return false;
        }
        return true;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
