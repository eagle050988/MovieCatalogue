package com.nasatech.moviecatalogue.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class TVShow implements Parcelable {

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
    private int id;
    private String original_name;
    private String name;
    private double popularity;
    private int vote_count;
    private String first_air_date;
    private String backdrop_path;
    private String original_language;
    private double vote_average;
    private String overview;
    private String poster_path;

    public TVShow(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.original_name = object.getString("original_name");
            this.name = object.getString("name");
            this.popularity = object.getDouble("popularity");
            this.vote_count = object.getInt("vote_count");
            this.first_air_date = object.getString("first_air_date");
            this.backdrop_path = "https://image.tmdb.org/t/p/w185" + object.getString("backdrop_path");
            this.original_language = object.getString("original_language");
            this.vote_average = object.getDouble("vote_average");
            this.overview = object.getString("overview");
            this.poster_path = "https://image.tmdb.org/t/p/w185" + object.getString("poster_path");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected TVShow(Parcel in) {
        this.id = in.readInt();
        this.original_name = in.readString();
        this.name = in.readString();
        this.popularity = in.readDouble();
        this.vote_count = in.readInt();
        this.first_air_date = in.readString();
        this.backdrop_path = in.readString();
        this.original_language = in.readString();
        this.vote_average = in.readDouble();
        this.overview = in.readString();
        this.poster_path = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.original_name);
        dest.writeString(this.name);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeString(this.first_air_date);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.original_language);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
    }
}
