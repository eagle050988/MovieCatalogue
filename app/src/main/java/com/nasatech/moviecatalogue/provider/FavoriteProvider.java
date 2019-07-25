package com.nasatech.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nasatech.moviecatalogue.db.FavoriteHelper;

import static com.nasatech.moviecatalogue.db.DatabaseFavorite.TABLE_FAVORITE;
import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_MOVIE;
import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_TVSHOW;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.AUTHORITY;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class FavoriteProvider extends ContentProvider {
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE, NOTE);
        // content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE + "/#", NOTE_ID);
    }

    private FavoriteHelper favoritHelper;

    @Override
    public boolean onCreate() {
        favoritHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoritHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                switch (Integer.parseInt(selection)) {
                    case TYPE_MOVIE:
                        cursor = favoritHelper.getAllMovies();
                        break;
                    case TYPE_TVSHOW:
                        cursor = favoritHelper.getAllTVShow();
                        break;
                    default:
                        cursor = favoritHelper.queryProvider();
                        break;
                }
                break;
            case NOTE_ID:
                cursor = favoritHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoritHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                added = favoritHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoritHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                deleted = favoritHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoritHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                updated = favoritHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
//        getContext().getContentResolver().notifyChange(CONTENT_URI, new MainActivity.DataObserver(new Handler(), getContext()));
        return updated;
    }
}
