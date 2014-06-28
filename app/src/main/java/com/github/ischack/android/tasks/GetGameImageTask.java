package com.github.ischack.android.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.model.Game;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by david on 6/27/14 for android
 */
public class GetGameImageTask extends AsyncTask<List<Game>, Integer, Void> {

    private GameGridListAdapter adapter;

    private Activity c;


    public GetGameImageTask(GameGridListAdapter adapter, Activity c) {

        this.adapter = adapter;

        this.c = c;

    }

    @Override
    protected Void doInBackground(List<Game>... games) {

        int[] res = new int[] {R.drawable.game_1, R.drawable.game_2, R.drawable.game_3, R.drawable.game_4, R.drawable.game_5, R.drawable.game_1, R.drawable.game_3, R.drawable.game_4, R.drawable.game_5 };

        for(int i = 0; i < res.length; i++) {
            Bitmap bm = BitmapFactory.decodeResource(c.getResources(), res[i]);

            final Game g = new Game();
            g.setName("Game " + i);
            g.setInformation("This is a game");
            g.setImage(bm);

            Log.d("ISCHACK", "Created game #" + i);
            //games[0].get(i).setImage(bm);

//            adapter.add(games[0].get(i));
            c.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(g);
                    adapter.notifyDataSetChanged();
                }
            });

        }

        return null;
    }
}
