package com.github.ischack.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.HeaderViewListAdapter;
import com.etsy.android.grid.StaggeredGridView;
import com.github.ischack.android.Dummy;
import com.github.ischack.android.GameActivity;
import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.fragments.gamefragment.GameFragment;
import com.github.ischack.android.model.Game;
import com.github.ischack.android.tasks.GetGameImageTask;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class GamesGridFragment extends Fragment implements AdapterView.OnItemClickListener{

    StaggeredGridView gridView;

    GameGridListAdapter mAdapter;

    public GamesGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (StaggeredGridView) view.findViewById(R.id.grid_view);

        mAdapter = new GameGridListAdapter(getActivity(), 0);
        gridView.setAdapter( mAdapter);

        gridView.setOnItemClickListener(this);

        new AsyncTask<Void, Void, ArrayList<Game>>() {

            @Override
            protected ArrayList<Game> doInBackground(Void... params) {
                HttpClient client = new DefaultHttpClient();

                HttpGet get = new HttpGet("http://numeric-dialect-623.appspot.com/event/b4b97db9-0fe1-463f-a85a-340d13cf84ed/games");

                HttpResponse response = null;
                try {
                    response = client.execute(get);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(response != null) {
                    StatusLine line = response.getStatusLine();
                    Log.i("ISCHACK", "complete: " + line);
                    // return code indicates upload failed so throw exception
                    if (line.getStatusCode() < 200 || line.getStatusCode() >= 300) {
                        return null; //TODO: Add an error dialog for error.
                    } else {

                        String jsontext = "";
                        try {
                            InputStream is = response.getEntity().getContent();
                            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                            jsontext = s.hasNext() ? s.next() : "";
                        } catch (Exception e) {
                            Log.wtf("Read", "Error");
                        }

                        ArrayList<Game> games = new ArrayList<Game>();
                        try {
                            JSONArray array = new JSONArray(jsontext);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                Game game = Game.createFromJson(obj);
                                games.add(game);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        return games;

                    }
                }
                return new ArrayList<Game>();
            }

            @Override
            protected void onPostExecute(ArrayList<Game> games) {
                super.onPostExecute(games);
                new GetGameImageTask(mAdapter, getActivity()).execute(games);
            }
        }.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Game game = (Game) view.getTag();

        Toast.makeText(getActivity(), "Clicked: " + game.getName(), Toast.LENGTH_LONG).show();


        if(getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).getString("user_type", "").equals("volunteer")) {
            Intent gameIntent = new Intent(getActivity(), GameActivity.class);
            gameIntent.putExtra("game", game);
            startActivity(gameIntent);
        } else
        {
            GameFragment gameFrag = GameFragment.newInstance(game);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, gameFrag, GameFragment.class.getName()).commit();
        }
    }
}
