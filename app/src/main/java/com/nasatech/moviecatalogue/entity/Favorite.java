package com.nasatech.moviecatalogue.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
    private int ID;
    private int TypeFavorite;
    private int IDEntity;
    private String Title;
    private String Description;
    private String Date;
    private String vote;
    private String Image;

    public Favorite() {
    }

    protected Favorite(Parcel in) {
        this.ID = in.readInt();
        this.TypeFavorite = in.readInt();
        this.Title = in.readString();
        this.Description = in.readString();
        this.Date = in.readString();
        this.Image = in.readString();
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public int getIDEntity() {
        return IDEntity;
    }

    public void setIDEntity(int IDEntity) {
        this.IDEntity = IDEntity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getTypeFavorite() {
        return TypeFavorite;
    }

    public void setTypeFavorite(int typeFavorite) {
        TypeFavorite = typeFavorite;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeInt(this.TypeFavorite);
        dest.writeString(this.Title);
        dest.writeString(this.Description);
        dest.writeString(this.Date);
        dest.writeString(this.Image);
    }
}
