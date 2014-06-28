package com.github.ischack.android.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;

/**
 * Created by david on 6/27/14 for android
 */
public class Game implements Parcelable {


    public static Game createFromJson(JSONObject obj) throws JSONException {
        Game game = new Game();
        game.setName(obj.getString("name"));
        game.setImageUrl(Uri.parse(obj.getString("gamePic")));
        game.setRules(obj.getString("rules"));
        game.setScoreType(ScoreType.valueOf(obj.getString("scoreType").toUpperCase()));
        game.setInformation(obj.getString("info"));

        return game;
    }

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

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return Integer.toString(name.hashCode());
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
        name = in.readString();
        imageUrl = in.readParcelable(Game.class.getClassLoader());
        rules = in.readString();
        information = in.readString();
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

