package com.punisher.fitnesstracker.database;

import android.provider.BaseColumns;

public class FitnessDBContract {

    public FitnessDBContract() {
        // empty, nothing to do
    }

    public static abstract class FitnessEntry implements BaseColumns {
        public static final String TABLE_NAME = "fitness";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_FITNESS_TYPE= "fitnesstype";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_NULLABLE = "";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String REAL_TYPE = " REAL";
        private static final String COMMA_SEP = ",";
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FitnessEntry.TABLE_NAME + " (" +
                        FitnessEntry._ID + " INTEGER PRIMARY KEY," +
                        FitnessEntry.COLUMN_NAME_DAY + REAL_TYPE + COMMA_SEP +
                        FitnessEntry.COLUMN_NAME_TIME + REAL_TYPE + COMMA_SEP +
                        FitnessEntry.COLUMN_NAME_FITNESS_TYPE + TEXT_TYPE + COMMA_SEP +
                        FitnessEntry.COLUMN_NAME_DURATION + INT_TYPE + COMMA_SEP +
                        FitnessEntry.COLUMN_NAME_DISTANCE + INT_TYPE +
                " )";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FitnessEntry.TABLE_NAME;
    }
}
