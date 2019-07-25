package com.nasatech.moviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;

import com.nasatech.moviecatalogue.entity.Favorite;
import com.nasatech.moviecatalogue.entity.Movie;
import com.nasatech.moviecatalogue.entity.TVShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DATE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DESCRIPTION;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IDEntity;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IMAGE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.TITLE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.Type;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.VOTE_COUNT;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.TABLE_FAVORITE;

public class FavoriteHelper {
    public static final int TYPE_MOVIE = 1;
    public static final int TYPE_TVSHOW = 2;
    private static final String DATABASE_TABLE = TABLE_FAVORITE;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

//    public ArrayList<Favorite> getAllMovies() {
//        ArrayList<Favorite> arrayList = new ArrayList<>();
//
//        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + Type + " = " + TYPE_MOVIE, null);
//        cursor.moveToFirst();
//        Favorite favorite;
//        if (cursor.getCount() > 0) {
//            do {
//                favorite = new Favorite();
//                favorite.setID(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
//                favorite.setTypeFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(Type)));
//                favorite.setIDEntity(cursor.getInt(cursor.getColumnIndexOrThrow(IDEntity)));
//                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
//                favorite.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
//                favorite.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
//                favorite.setImage(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE)));
//                favorite.setVote_Average(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
//
//                arrayList.add(favorite);
//                cursor.moveToNext();
//            } while (!(cursor.isAfterLast()));
//        }
//        cursor.close();
//        return arrayList;
//    }

    public Cursor getAllMovies() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + Type + " = " + TYPE_MOVIE, null);

        return cursor;
    }

//    public ArrayList<Favorite> getAllTVShow() {
//        ArrayList<Favorite> arrayList = new ArrayList<>();
//
//        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + Type + " = " + TYPE_TVSHOW, null);
//        cursor.moveToFirst();
//        Favorite favorite;
//        if (cursor.getCount() > 0) {
//            do {
//                favorite = new Favorite();
//                favorite.setID(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
//                favorite.setTypeFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(Type)));
//                favorite.setIDEntity(cursor.getInt(cursor.getColumnIndexOrThrow(IDEntity)));
//                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
//                favorite.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
//                favorite.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
//                favorite.setImage(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE)));
//                favorite.setVote_Average(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
//
//                arrayList.add(favorite);
//                cursor.moveToNext();
//            } while (!(cursor.isAfterLast()));
//        }
//        cursor.close();
//        return arrayList;
//    }

    public Cursor getAllTVShow() {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + Type + " = " + TYPE_TVSHOW, null);

        return cursor;
    }

    public ArrayList<Favorite> getAllItem() {
        ArrayList<Favorite> arrayList = new ArrayList<>();

        final long identityToken = Binder.clearCallingIdentity();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite();
                favorite.setID(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favorite.setTypeFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(Type)));
                favorite.setIDEntity(cursor.getInt(cursor.getColumnIndexOrThrow(IDEntity)));
                favorite.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favorite.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                favorite.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favorite.setImage(cursor.getString(cursor.getColumnIndexOrThrow(IMAGE)));
                favorite.setVote_Average(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_COUNT)));

                arrayList.add(favorite);
                cursor.moveToNext();
            } while (!(cursor.isAfterLast()));
        }
        cursor.close();

        Binder.restoreCallingIdentity(identityToken);
        return arrayList;
    }

    public long insertFavorite(Movie movie) {
        final int result = cekData(movie.getId());

        if (result > 0)
            return -3;

        ContentValues args = new ContentValues();
        args.put(Type, TYPE_MOVIE);
        args.put(IDEntity, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(DESCRIPTION, movie.getOverview());
        args.put(DATE, movie.getRelease_date());
        args.put(IMAGE, movie.getPoster_path());
        args.put(VOTE_COUNT, String.valueOf(movie.getVote_average()));
        return database.insert(DATABASE_TABLE, null, args);
    }

    public long insertFavoriteTVShow(TVShow tvShow) {
        final int result = cekData(tvShow.getId());

        if (result > 0)
            return -3;

        ContentValues args = new ContentValues();
        args.put(Type, TYPE_TVSHOW);
        args.put(IDEntity, tvShow.getId());
        args.put(TITLE, tvShow.getName());
        args.put(DESCRIPTION, tvShow.getOverview());
        args.put(DATE, tvShow.getFirst_air_date());
        args.put(IMAGE, tvShow.getPoster_path());
        args.put(VOTE_COUNT, String.valueOf(tvShow.getVote_average()));
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int updateFavorite(Favorite note) {
        ContentValues args = new ContentValues();
        args.put(Type, TYPE_MOVIE);
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.getID() + "'", null);
    }

    public int deleteMovie(int id) {
        database.execSQL("delete from " + DATABASE_TABLE + " WHERE " + IDEntity + " = '" + id + "' and " + Type + " = '" + TYPE_MOVIE + "'");
        return 1;
    }

    public int deleteTVShow(int id) {
        database.execSQL("delete from " + DATABASE_TABLE + " WHERE " + IDEntity + " = '" + id + "' and " + Type + " = '" + TYPE_TVSHOW + "'");
        return 1;
    }

    public int cekData(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + IDEntity + " = " + id, null);

        return cursor.getCount();
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , IDEntity + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        final int result = cekData(values.getAsInteger(IDEntity));

        if (result > 0)
            return -3;

        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, IDEntity + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, IDEntity + " = ?", new String[]{id});
    }
}
