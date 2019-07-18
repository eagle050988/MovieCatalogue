package com.nasatech.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.db.DatabaseHelper;
import com.nasatech.moviecatalogue.entity.Favorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DATE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DESCRIPTION;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IDEntity;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IMAGE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.TITLE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.Type;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.TABLE_FAVORITE;

public class StackRemoteService implements RemoteViewsService.RemoteViewsFactory {
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;
    private final Context mContext;
    private ArrayList<Favorite> FavoritTVShow = new ArrayList<>();
    private Cursor cursor;

    public StackRemoteService(Context context) {
        mContext = context;
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }


        final long identityToken = Binder.clearCallingIdentity();

        cursor = database.rawQuery("SELECT * FROM " + TABLE_FAVORITE, null);
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

                FavoritTVShow.add(favorite);
                cursor.moveToNext();
            } while (!(cursor.isAfterLast()));
        }
        cursor.close();

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return FavoritTVShow.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (FavoritTVShow.size() > 0) {
            try {
                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(FavoritTVShow.get(position).getImage())
                        .submit(512, 512)
                        .get();

                rv.setImageViewBitmap(R.id.imageView, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
