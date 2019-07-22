package com.nasatech.moviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String SQL_CREATE_TABLE_NOTE = "CREATE TABLE " + DatabaseFavorite.TABLE_FAVORITE
            + " (" + DatabaseFavorite.FavoriteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseFavorite.FavoriteColumns.Type + " INTEGER NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.IDEntity + " INTEGER NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.TITLE + " TEXT NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.DESCRIPTION + " TEXT NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.DATE + " TEXT NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.IMAGE + " TEXT NOT NULL, " +
            DatabaseFavorite.FavoriteColumns.VOTE_COUNT + " TEXT NOT NULL)";

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "dbfavoriteapp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseFavorite.TABLE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseFavorite.TABLE_FAVORITE);
        onCreate(db);
    }
}
