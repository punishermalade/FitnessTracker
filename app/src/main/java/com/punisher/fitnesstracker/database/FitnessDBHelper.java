package com.punisher.fitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class FitnessDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FitnessTracker.db";
    public static final int DATABASE_VERSION = 1;

    public FitnessDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FitnessDBContract.FitnessEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FitnessDBContract.FitnessEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
