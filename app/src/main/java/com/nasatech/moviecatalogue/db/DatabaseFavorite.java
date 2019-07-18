package com.nasatech.moviecatalogue.db;

import android.provider.BaseColumns;

public class DatabaseFavorite {
    public static String TABLE_FAVORITE = "favorite";

    public static final class FavoriteColumns implements BaseColumns {
        public static String Type = "typefavorite";
        public static String IDEntity = "identity";
        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
        public static String IMAGE = "image";
    }
}
