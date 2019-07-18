package com.nasatech.moviecatalogue.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nasatech.moviecatalogue.R;
import com.nasatech.moviecatalogue.entity.Movie;

public class DetailMovie extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    static final String KEY_MOVIE = "movie";
    Movie movie;

    private ProgressBar progressBar;
    private TextView txtName;
    private TextView txtProduction;
    private TextView txtDescription;
    private ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if (savedInstanceState == null) {
            movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        } else {
            movie = savedInstanceState.getParcelable(KEY_MOVIE);
        }

        progressBar = findViewById(R.id.progressBar);
        txtName = findViewById(R.id.txt_name);
        txtProduction = findViewById(R.id.txt_film_production);
        txtDescription = findViewById(R.id.txt_description);
        imgPhoto = findViewById(R.id.img_photo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail Movie");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        showLoading(true);
        txtDescription.setText(movie.getOverview());
        txtName.setText(movie.getTitle());
        String text = String.format(getResources().getString(R.string.porductionmovie), movie.getRelease_date(), movie.getVote_average(), movie.getOriginal_language());
        txtProduction.setText(text);

        Glide.with(this)
                .load(movie.getPoster_path())
                .apply(new RequestOptions().override(185, 278))
                .into(imgPhoto);
        showLoading(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_MOVIE, movie);
        super.onSaveInstanceState(outState);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
