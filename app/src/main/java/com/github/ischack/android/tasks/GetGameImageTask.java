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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
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
        //TODO: Make this iterate over given game objects in list.

        for(int i = 1; i <= 20; i++) {

            Bitmap bm = null;

            HttpGet httpget = new HttpGet("http://placekitten.com/g/540/" + (500 + new Random().nextInt(400)));

            HttpClient client = new DefaultHttpClient();

            HttpResponse response = null;
            try {
                response = client.execute(httpget);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response != null) {
                StatusLine line = response.getStatusLine();
                Log.i("ISCHACK", "complete: " + line);
                // return code indicates upload failed so throw exception
                if (line.getStatusCode() < 200 || line.getStatusCode() >= 300) {
                    Log.e("ISCHACK", "Failed to get image with error code: " + line.getStatusCode());
                    return null; //TODO: Add an error dialog for error.
                } else {

                    try {
                        // A Simple JSON Response Read
                        InputStream instream = response.getEntity().getContent();

                        bm = BitmapFactory.decodeStream(instream);

                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(bm == null)
                            bm = BitmapFactory.decodeResource(c.getResources(), R.drawable.ic_launcher);
                    }
                }
            }
            final Game g = new Game();
            g.setName("Game " + i);

            switch(i % 3) {
                case 0:
                    g.setScoreType(Game.ScoreType.COUNT);
                    break;
                case 1:
                    g.setScoreType(Game.ScoreType.TIME);
                    break;
                case 2:
                    g.setScoreType(Game.ScoreType.DISTANCE);
                    break;
            }


            g.setInformation("This is a game");
            g.setImage(bm);

            Log.d("ISCHACK", "Created game #" + i);
            //games[0].get(i).setImage(bm);

//            adapter.add(games[0].get(i));
            c.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("ISCHACK", "Added game: " + g.getName());
                    adapter.add(g);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return null;
    }
}
