package com.ostap_kozak.progress.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ostap_kozak.progress.model.Task;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by ostapkozak on 10/09/2016.
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasksDatabase.db";
    private static final int DATABASE_VERSION = 1;


    public TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register out models
        cupboard().register(Task.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // Add indexes and other database tweaks in this method if you want

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // Do migration work if you have an alteration to make to your schema here.
    }
}
