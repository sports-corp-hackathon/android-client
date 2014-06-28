package com.github.ischack.android.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;

/**
 * Created by david on 6/27/14 for android
 */
public class Game implements Parcelable {

    public static enum ScoreType {
        TIME, RANK, BOOL, DISTANCE, COUNT;
    }

    private ScoreType scoreType;
    private String name;
    private Uri imageUrl;
    private Bitmap image;
    private String rules;
    private String information;

    public Game() {
        name = "<GAME NAME>";
        scoreType = ScoreType.COUNT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap b) {
        image = b;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(imageUrl, flags);
        dest.writeString(rules);
        dest.writeString(information);
        dest.writeString(scoreType.toString());
    }


    private Game(Parcel in) {
        information = in.readString();
        rules = in.readString();
        imageUrl = in.readParcelable(Game.class.getClassLoader());
        name = in.readString();
        scoreType = ScoreType.valueOf(in.readString());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}

