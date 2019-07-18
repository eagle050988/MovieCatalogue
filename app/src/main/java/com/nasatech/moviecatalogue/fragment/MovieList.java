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
import com.nasatech.moviecatalogue.activity.DetailMovie;
import com.nasatech.moviecatalogue.adapter.MovieAdapter;
import com.nasatech.moviecatalogue.entity.Movie;
import com.nasatech.moviecatalogue.ui.main.PageViewModelMovie;

import java.util.ArrayList;

public class MovieList extends Fragment {

    public String ClassName;
    private PageViewModelMovie pageViewModelMovie;
    private RecyclerView rvCategory;
    private ProgressBar progressBar;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Movie> AllMovies;
    private Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movie> movieItems) {
            if (movieItems != null) {
                movies = movieItems;
                showRecyclerList();
                showLoading(false);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelMovie = ViewModelProviders.of(this).get(PageViewModelMovie.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        rvCategory = view.findViewById(R.id.rv_movie);
        rvCategory.setHasFixedSize(true);

        showLoading(true);


        if (savedInstanceState == null) {
            pageViewModelMovie.setData();
            pageViewModelMovie.getMovies().observe(this, getMovies);

            if (AllMovies == null)
                AllMovies = new ArrayList<>(movies);
        } else {
            movies = savedInstanceState.getParcelableArrayList(DetailMovie.EXTRA_MOVIE);
            showRecyclerList();

            AllMovies = savedInstanceState.getParcelableArrayList("AllMovies");
            showLoading(false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(DetailMovie.EXTRA_MOVIE, movies);
        outState.putParcelableArrayList("AllMovies", AllMovies);
        super.onSaveInstanceState(outState);
    }

    public void StartSearch(String data) {
        if (AllMovies.size() == 0 && movies.size() > 0)
            AllMovies.addAll(movies);

        if (data == "") {
            ResetData();
        } else {
            pageViewModelMovie.Search(data);
            pageViewModelMovie.getMovies().observe(this, getMovies);
        }
    }

    public void ResetData() {
        movies = AllMovies;
        showRecyclerList();
    }

    private void showRecyclerList() {
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        MovieAdapter movieAdapter = new MovieAdapter(getActivity());
        movieAdapter.setMovies(movies);
        rvCategory.setAdapter(movieAdapter);

        movieAdapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie data) {
                showSelectedMovie(data);
            }
        });
    }

    private void showSelectedMovie(Movie movie) {
        Intent moveWithObjectIntent = new Intent(getActivity(), DetailMovie.class);
        moveWithObjectIntent.putExtra(DetailMovie.EXTRA_MOVIE, movie);
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
