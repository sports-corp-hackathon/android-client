package com.github.ischack.android.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.github.ischack.android.R;
import com.github.ischack.android.model.Game;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by david on 6/28/14.
 */
public class GameDownloadHelper {


    public static Bitmap downloadImageForGame(Context c, Game g)
    {
        Bitmap bm = null;

        Log.d("ISCHACK", "URI: " + g.getImageUrl());
        HttpGet httpget = new HttpGet(g.getImageUrl().toString());

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

        return bm;
    }
}
