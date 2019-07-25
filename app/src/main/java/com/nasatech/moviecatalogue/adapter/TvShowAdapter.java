package com.nasatech.moviecatalogue.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.db.DatabaseFavorite;
import com.nasatech.moviecatalogue.db.FavoriteHelper;
import com.nasatech.moviecatalogue.entity.TVShow;

import java.util.ArrayList;

import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DATE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DESCRIPTION;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IDEntity;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IMAGE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.TITLE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.Type;
import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_TVSHOW;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TVShow> movies;

    private TvShowAdapter.OnItemClickCallback onItemClickCallback;

    public TvShowAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    public void setOnItemClickCallback(TvShowAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<TVShow> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<TVShow> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv_show, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        holder.txtName.setText(getMovies().get(i).getName());
        holder.txtDescription.setText(getMovies().get(i).getOverview());

        holder.tvShow = getMovies().get(i);

        Glide.with(context)
                .load(getMovies().get(i).getPoster_path())
                .apply(new RequestOptions().override(185, 278))
                .into(holder.imgPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(movies.get(holder.getAdapterPosition()));
            }
        });

        Uri uri = Uri.parse(CONTENT_URI + "/" + getMovies().get(i).getId());
        Cursor result = getContext().getContentResolver().query(uri, null, null, null, null);

//        if (holder.favoriteHelper.cekData(getMovies().get(i).getId()) > 0) {
        if (result.getCount() > 0) {
            holder.btnFavorite.setEnabled(false);
            holder.btndelete.setEnabled(true);
        } else {
            holder.btnFavorite.setEnabled(true);
            holder.btndelete.setEnabled(false);
        }
    }


    @Override
    public int getItemCount() {
        return getMovies().size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TVShow data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FavoriteHelper favoriteHelper;
        TextView txtName;
        TextView txtDescription;
        ImageView imgPhoto;
        Button btnFavorite, btndelete;
        private TVShow tvShow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_item_name);
            txtDescription = itemView.findViewById(R.id.tv_item_remarks);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
            btndelete = itemView.findViewById(R.id.btn_delete_favorite);

            favoriteHelper = FavoriteHelper.getInstance(getContext());

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    long result = favoriteHelper.insertFavoriteTVShow(tvShow);

                    ContentValues args = new ContentValues();
                    args.put(Type, TYPE_TVSHOW);
                    args.put(IDEntity, tvShow.getId());
                    args.put(TITLE, tvShow.getName());
                    args.put(DESCRIPTION, tvShow.getOverview());
                    args.put(DATE, tvShow.getFirst_air_date());
                    args.put(IMAGE, tvShow.getPoster_path());
                    args.put(DatabaseFavorite.FavoriteColumns.VOTE_COUNT, String.valueOf(tvShow.getVote_average()));

                    Uri uri = getContext().getContentResolver().insert(CONTENT_URI, args);

                    long result = Long.parseLong(uri.getLastPathSegment());

                    if (result > 0) {
                        Toast.makeText(getContext(), "Success Save to Favorite " + tvShow.getName(), Toast.LENGTH_LONG).show();
                        btnFavorite.setEnabled(false);
                        btndelete.setEnabled(true);
                    } else if (result == -3) {
                        Toast.makeText(getContext(), tvShow.getName() + "Has been set to Favorite", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Failed Save to Favorite", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri contenturi = Uri.parse(CONTENT_URI + "/" + tvShow.getId());
//                    favoriteHelper.deleteTVShow(tvShow.getId());

                    int result = getContext().getContentResolver().delete(contenturi, null, null);
                    if (result > 0) {
                        Toast.makeText(getContext(), "Success Delete from Favorite " + tvShow.getName(), Toast.LENGTH_LONG).show();
                        btnFavorite.setEnabled(true);
                        btndelete.setEnabled(false);
                    } else {
                        Toast.makeText(getContext(), "Failed Delete from Favorite " + tvShow.getName(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void setMovie(TVShow tvShow) {
            this.tvShow = tvShow;
        }
    }
}
