package com.nasatech.moviecatalogue.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.activity.FavoriteDetail;
import com.nasatech.moviecatalogue.adapter.FavoriteMovieAdapter;
import com.nasatech.moviecatalogue.db.FavoriteHelper;
import com.nasatech.moviecatalogue.entity.Favorite;

import java.util.ArrayList;

import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_MOVIE;
import static com.nasatech.moviecatalogue.helper.MappingHelper.mapCursorToArrayList;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class FavoriteMovie extends Fragment {
    private ArrayList<Favorite> FavoriteMovies = new ArrayList<>();
    private RecyclerView rvCategory;
    private FavoriteHelper favoriteHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteHelper = FavoriteHelper.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCategory = view.findViewById(R.id.movie_list);
        rvCategory.setHasFixedSize(true);

        if (savedInstanceState == null) {
            Cursor result = getContext().getContentResolver().query(CONTENT_URI, null, String.valueOf(TYPE_MOVIE), null, null);
//            FavoriteMovies = favoriteHelper.getAllMovies();
            FavoriteMovies = mapCursorToArrayList(result);
        } else {
            FavoriteMovies = savedInstanceState.getParcelableArrayList("favoritemovie");
        }

        showRecyclerList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("favoritemovie", FavoriteMovies);
        super.onSaveInstanceState(outState);
    }

    private void showRecyclerList() {
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavoriteMovieAdapter movieAdapter = new FavoriteMovieAdapter(getActivity());
        movieAdapter.setMovies(FavoriteMovies);
        rvCategory.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback(new FavoriteMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Favorite data) {
                showSelectedMovie(data);
            }
        });
    }

    private void showSelectedMovie(Favorite movie) {
        Intent moveWithObjectIntent = new Intent(getActivity(), FavoriteDetail.class);
        moveWithObjectIntent.putExtra("favorite", movie);
        startActivity(moveWithObjectIntent);
    }
}
