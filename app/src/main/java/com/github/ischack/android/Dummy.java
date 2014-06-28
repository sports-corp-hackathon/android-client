package com.github.ischack.android;

import android.net.Uri;
import android.util.Log;

import com.github.ischack.android.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by david on 6/28/14.
 */
public class Dummy {

    public static List<Game> getDummyGames() {

        List<Game> games = new ArrayList<Game>();

        for(int i = 0; i < 20; i++) {
            Game g = new Game();

            g.setScoreType(getRandType());

            g.setInformation(getRandomInfo());

            g.setName(getRandomName());

            g.setRules(getRandomRules());

            Uri uri = Uri.parse("http://placekitten.com/g/540/" + (500 + new Random().nextInt(400)));

            g.setImageUrl(uri);

            Log.d("ISCHACK", "Uri at add: " + uri + " and string " + uri.toString() + " and in game " + g.getImageUrl());

            games.add(g);
        }
        return games;
    }

    private static Game.ScoreType getRandType()
    {
        int num = Game.ScoreType.values().length;
        return Game.ScoreType.values()[ new Random().nextInt(num) ];
    }

    private static String getRandomName()
    {
        String[] names = new String[] { "Cornhole", "Tetris", "Mario", "Ladderfall", "Football", "Chair Throw" };
        return (String) getRandomArray(names);
    }

    private static String getRandomRules()
    {
        String[] names = new String[] { "Step 1: Play game\nStep 2: Have fun", "Step 1: Play game\n" +
                "Step 2: Have fun\n???\nStep 4: Profit", "Be like Mario, talk like mario, jump like mario",
                "Climbing and climbing and then you fall",
                "Throw the ball and dont get killed.", "Rage!!!! Throw stuff and try to break it!!" };
        return (String) getRandomArray(names);
    }

    private static String getRandomInfo()
    {
        String[] names = new String[] { "Stuff", "Stuff and things", "Testing", "I am out of ideas", "This is text", "More text that I have" };
        return (String) getRandomArray(names);
    }

    private static Object getRandomArray(Object[] arr )
    {
        int num = arr.length;
        return arr[ new Random().nextInt(num) ];
    }
}
