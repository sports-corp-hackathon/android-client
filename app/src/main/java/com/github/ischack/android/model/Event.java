package com.github.ischack.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 6/27/14 for android
 */
public class Event {
    List<Game> games;

    public Event() {

    }

    public void addGame(Game game)
    {
        if(games == null) {
            games = new ArrayList<Game>();
        }
        games.add(game);
    }

    public List<Game> getGames() {

        return games;
    }

}
