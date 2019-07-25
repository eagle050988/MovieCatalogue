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
import com.nasatech.moviecatalogue.db.FavoriteHelper;
import com.nasatech.moviecatalogue.entity.Movie;

import java.util.ArrayList;

import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DATE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.DESCRIPTION;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IDEntity;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.IMAGE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.TITLE;
import static com.nasatech.moviecatalogue.db.DatabaseFavorite.FavoriteColumns.Type;
import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_MOVIE;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.VOTE_COUNT;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;

    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        holder.setMovie(getMovies().get(i));
        holder.txtName.setText(getMovies().get(i).getTitle());
        holder.txtDescription.setText(getMovies().get(i).getOverview());

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

        //if (holder.favoriteHelper.cekData(getMovies().get(i).getId()) > 0) {
        Uri uri = Uri.parse(CONTENT_URI + "/" + getMovies().get(i).getId());
        Cursor result = getContext().getContentResolver().query(uri, null, null, null, null);

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
        void onItemClicked(Movie data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public FavoriteHelper favoriteHelper;
        TextView txtName;
        TextView txtDescription;
        ImageView imgPhoto;
        Button btnFavorite, btndelete;
        private Movie movie;

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
                    ContentValues args = new ContentValues();
                    args.put(Type, TYPE_MOVIE);
                    args.put(IDEntity, movie.getId());
                    args.put(TITLE, movie.getTitle());
                    args.put(DESCRIPTION, movie.getOverview());
                    args.put(DATE, movie.getRelease_date());
                    args.put(IMAGE, movie.getPoster_path());
                    args.put(VOTE_COUNT, String.valueOf(movie.getVote_average()));

                    Uri uri = getContext().getContentResolver().insert(CONTENT_URI, args);

//                    uri.getLastPathSegment();
//                    long result = favoriteHelper.insertFavorite(movie);
                    long result = Long.parseLong(uri.getLastPathSegment());

                    if (result > 0) {
                        Toast.makeText(getContext(), "Success Save to Favorite " + movie.getTitle(), Toast.LENGTH_LONG).show();
                        btnFavorite.setEnabled(false);
                        btndelete.setEnabled(true);
                    } else if (result == -3) {
                        Toast.makeText(getContext(), movie.getTitle() + "Has been set to Favorite", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Failed Save to Favorite", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri contenturi = Uri.parse(CONTENT_URI + "/" + movie.getId());

//                    favoriteHelper.deleteMovie(movie.getId());
                    int result = getContext().getContentResolver().delete(contenturi, null, null);

                    if (result > 0) {
                        btnFavorite.setEnabled(true);
                        btndelete.setEnabled(false);
                        Toast.makeText(getContext(), "Success Delete from Favorite " + movie.getTitle(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Failed Delete from Favorite " + movie.getTitle(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }
    }
}
