package com.github.ischack.android.model;

/**
 * Created by david on 6/28/14.
 */
public class GameScore {
    private Game game;
    private String score;

    public GameScore(Game game, String score) {
        this.game = game;
        this.score = score;
    }

    public Game getGame() {
        return game;
    }

    public String getScore() {
        return score;
    }
}
