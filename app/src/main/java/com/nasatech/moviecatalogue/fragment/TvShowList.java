package com.nasatech.moviecatalogue.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.activity.DetailTVShow;
import com.nasatech.moviecatalogue.adapter.TvShowAdapter;
import com.nasatech.moviecatalogue.entity.TVShow;
import com.nasatech.moviecatalogue.ui.main.PageViewModelTVShow;

import java.util.ArrayList;


public class TvShowList extends Fragment {
    public String ClassName;
    private PageViewModelTVShow pageViewModelTVShow;
    private ProgressBar progressBar;
    private RecyclerView rvCategory;
    private ArrayList<TVShow> TVShowlist = new ArrayList<>();
    private ArrayList<TVShow> AllTVShow;
    private Observer<ArrayList<TVShow>> getTVShows = new Observer<ArrayList<TVShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TVShow> TVShowItems) {
            if (TVShowItems != null) {
                TVShowlist = TVShowItems;
                showRecyclerList();
                showLoading(false);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelTVShow = ViewModelProviders.of(this).get(PageViewModelTVShow.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        rvCategory = view.findViewById(R.id.rv_tv_show);
        rvCategory.setHasFixedSize(true);

        showLoading(true);


        if (savedInstanceState == null) {
            pageViewModelTVShow.setData();
            pageViewModelTVShow.getTVShows().observe(this, getTVShows);
            if (AllTVShow == null)
                AllTVShow = new ArrayList<>(TVShowlist);
        } else {
            TVShowlist = savedInstanceState.getParcelableArrayList(DetailTVShow.EXTRA_TVSHOW);
            showRecyclerList();

            AllTVShow = savedInstanceState.getParcelableArrayList("AllTVShow");

            showLoading(false);
        }
    }

    public void StartSearch(String data) {
        if (AllTVShow.size() == 0 && TVShowlist.size() > 0)
            AllTVShow.addAll(TVShowlist);

        if (data == "") {
            ResetData();
        } else {
            pageViewModelTVShow.Search(data);
            pageViewModelTVShow.getTVShows().observe(this, getTVShows);
        }
    }

    public void ResetData() {
        TVShowlist = AllTVShow;
        showRecyclerList();
    }

    private void showRecyclerList() {
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        TvShowAdapter tvShowAdapter = new TvShowAdapter(getActivity());
        tvShowAdapter.setMovies(TVShowlist);
        rvCategory.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TVShow data) {
                showSelectedMovie(data);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(DetailTVShow.EXTRA_TVSHOW, TVShowlist);
        outState.putParcelableArrayList("AllTVShow", AllTVShow);
        super.onSaveInstanceState(outState);
    }

    private void showSelectedMovie(TVShow movie) {
        Intent moveWithObjectIntent = new Intent(getActivity(), DetailTVShow.class);
        moveWithObjectIntent.putExtra(DetailTVShow.EXTRA_TVSHOW, movie);
        startActivity(moveWithObjectIntent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
