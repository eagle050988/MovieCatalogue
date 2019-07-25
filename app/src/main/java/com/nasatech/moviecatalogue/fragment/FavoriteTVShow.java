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
import com.nasatech.moviecatalogue.adapter.FavoriteTVShowAdapter;
import com.nasatech.moviecatalogue.db.FavoriteHelper;
import com.nasatech.moviecatalogue.entity.Favorite;

import java.util.ArrayList;

import static com.nasatech.moviecatalogue.db.FavoriteHelper.TYPE_TVSHOW;
import static com.nasatech.moviecatalogue.helper.MappingHelper.mapCursorToArrayList;
import static com.nasatech.moviecatalogue.provider.DatabaseContract.FavoriteColumns.CONTENT_URI;


public class FavoriteTVShow extends Fragment {
    private ArrayList<Favorite> FavoritTVShow = new ArrayList<>();
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
        return inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategory = view.findViewById(R.id.movie_list);
        rvCategory.setHasFixedSize(true);

        if (savedInstanceState == null) {
//            FavoritTVShow = favoriteHelper.getAllTVShow();
            Cursor result = getContext().getContentResolver().query(CONTENT_URI, null, String.valueOf(TYPE_TVSHOW), null, null);
            FavoritTVShow = mapCursorToArrayList(result);
        } else {
            FavoritTVShow = savedInstanceState.getParcelableArrayList("favoritetvshow");
        }

        showRecyclerList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("favoritetvshow", FavoritTVShow);
        super.onSaveInstanceState(outState);
    }

    private void showRecyclerList() {
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavoriteTVShowAdapter tvShowAdapter = new FavoriteTVShowAdapter(getActivity());
        tvShowAdapter.setFavoritTVShow(FavoritTVShow);
        rvCategory.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnItemClickCallback(new FavoriteTVShowAdapter.OnItemClickCallback() {
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
