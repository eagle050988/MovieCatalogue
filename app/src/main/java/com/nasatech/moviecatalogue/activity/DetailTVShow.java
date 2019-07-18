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
import com.nasatech.moviecatalogue.entity.TVShow;

public class DetailTVShow extends AppCompatActivity {
    public static final String EXTRA_TVSHOW = "extra_tvshow";
    static final String KEY_TVSHOW = "tvshow";
    TVShow tvShow;

    private ProgressBar progressBar;
    private TextView txtName;
    private TextView txtProduction;
    private TextView txtDescription;
    private ImageView imgPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailtvshow);

        if (savedInstanceState == null) {
            tvShow = getIntent().getParcelableExtra(EXTRA_TVSHOW);
        } else {
            tvShow = savedInstanceState.getParcelable(KEY_TVSHOW);
        }

        progressBar = findViewById(R.id.progressBar);
        txtName = findViewById(R.id.txt_name);
        txtProduction = findViewById(R.id.txt_film_production);
        txtDescription = findViewById(R.id.txt_description);
        imgPhoto = findViewById(R.id.img_photo);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail TV Show");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        showLoading(true);
        txtDescription.setText(tvShow.getOverview());
        txtName.setText(tvShow.getName());
        String text = String.format(getResources().getString(R.string.porductiontvshow), tvShow.getFirst_air_date(), tvShow.getVote_average(), tvShow.getOriginal_language());
        txtProduction.setText(text);

        Glide.with(this)
                .load(tvShow.getPoster_path())
                .apply(new RequestOptions().override(185, 278))
                .into(imgPhoto);
        showLoading(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_TVSHOW, tvShow);
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
