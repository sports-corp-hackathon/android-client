package com.github.ischack.android;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.ischack.android.fragments.GamesGridFragment;
import com.github.ischack.android.fragments.VolunteerLoginFragment;
import com.github.ischack.android.helpers.NfcUtils;
import com.github.ischack.android.model.Game;
import com.github.ischack.android.model.GameScore;

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


public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawer();

        if(!getSharedPreferences("profile", MODE_PRIVATE).contains("user_type"))
            getSharedPreferences("profile", MODE_PRIVATE).edit().putString("user_type", "basic").apply();

        getSupportFragmentManager().beginTransaction().add(R.id.content, new WelcomeFragment(), WelcomeFragment.class.getName()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        super.onResume();
        Log.e("ISCHACK", "Action: " + getIntent().getAction());

        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey(NfcAdapter.EXTRA_ID)) {
            Log.d("NFC", NfcUtils.ByteArrayToHexString(bundle.getByteArray(NfcAdapter.EXTRA_ID)));

            //TODO: Get the scores for the user here.
            new AsyncTask<Void, Void, List<GameScore>>() {

                @Override
                protected List<GameScore> doInBackground(Void... params) {

                    List<GameScore> scores = new ArrayList<GameScore>();

                    HttpGet httpget = new HttpGet("http://echo.jsontest.com/game/object/score/124134");

                    HttpClient client = new DefaultHttpClient();

                    HttpResponse response = null;
                    try {
                        response = client.execute(httpget);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (response != null) {
                        StatusLine line = response.getStatusLine();
                        Log.i("ISCHACK", "complete: " + line);
                        // return code indicates upload failed so throw exception
                        if (line.getStatusCode() < 200 || line.getStatusCode() >= 300) {
                            Log.e("ISCHACK", "Failed to get image with error code: " + line.getStatusCode());
                            return null;
                        } else {

                            String jsontext = "";
                            try {
                                InputStream is = response.getEntity().getContent();
                                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                                jsontext = s.hasNext() ? s.next() : "";
                            } catch (Exception e) {
                                Log.wtf("Read", "Error");
                            }

                            try {
                                JSONArray array = new JSONArray(jsontext);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject obj = array.getJSONObject(i);
                                    JSONObject gameJson = obj.getJSONObject("game");

                                    Game game = Game.createFromJson(gameJson);
                                    String score = obj.getString("score");

                                    scores.add(new GameScore(game, score));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return scores;
                }

                @Override
                protected void onPostExecute(List<GameScore> gameScores) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, ResultsFragment.getInstance(gameScores), ResultsFragment.class.getName()).commit();
                }
            }.execute();
        }

        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            Log.d("ISCHACK", String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }

        Intent i = getIntent();
        IntentFilter filter = new IntentFilter();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, NfcUtils.techList);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(i.getAction())) {
            Toast.makeText(this, NfcUtils.ByteArrayToHexString(i.getByteArrayExtra(NfcAdapter.EXTRA_ID)), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.volunteer_login, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.d("ISCHACK", "Item ("+position+") on grid was clicked.");
            selectItem(position);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);


//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);


        return super.onPrepareOptionsMenu(menu);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        //TODO: Work with different selections to push fragments
        // Create a new fragment and specify the planet to show based on position

        Fragment fragment = null;
        if(position == 0) {
            fragment = new VolunteerLoginFragment();
        } else if( position == 1) {
            fragment = new GamesGridFragment();
        }

        if( fragment != null ) {

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);

            setTitle(mDrawerList.getAdapter().getItem(position).toString());

            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[] {"Volunteer Login", "Games"}));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
}
