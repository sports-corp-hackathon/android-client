package com.github.ischack.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david on 6/28/14.
 */
public class GameScore implements Parcelable {

    private Game game;
    private String score;

    public GameScore(Game game, String score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public String toString() {
        return game.getName() + " " + score;
    }

    public Game getGame() {
        return game;
    }

    public String getScore() {
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(game, flags);
        dest.writeString(score);
    }

    private GameScore(Parcel in) {
        game = in.readParcelable(GameScore.class.getClassLoader());
        score = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GameScore createFromParcel(Parcel in) {
            return new GameScore(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
