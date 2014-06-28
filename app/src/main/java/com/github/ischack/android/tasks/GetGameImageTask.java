package com.github.ischack.android.tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.helpers.GameDownloadHelper;
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

        for(final Game g : games[0]) {

            Bitmap bm = GameDownloadHelper.downloadImageForGame(c, g);

            g.setImage(bm);

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
