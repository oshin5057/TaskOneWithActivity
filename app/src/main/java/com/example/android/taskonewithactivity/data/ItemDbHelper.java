package com.example.android.taskonewithactivity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

    public static final String DB_Name = "itemslist.db";

    public static final int DATABASE_VERSION = 1;

    public ItemDbHelper(Context context){
        super(context, DB_Name, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " ("
                + ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_EMAIL_ID + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ADDRESS + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_WEIGHT + " REAL NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_HEIGHT + " REAL NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_PHONE + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_DATE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemContract.ItemEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}