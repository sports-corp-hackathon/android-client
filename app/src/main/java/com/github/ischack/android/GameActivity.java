package com.github.ischack.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.github.ischack.android.R;
import com.github.ischack.android.fragments.GameFragment;
import com.github.ischack.android.model.Game;

public class GameActivity extends FragmentActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = getIntent().getExtras().getParcelable("game");

        getSupportFragmentManager().beginTransaction().add(R.id.content, GameFragment.newInstance(game), GameFragment.class.getName()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ISCHACK", "Action: " + getIntent().getAction());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("ISCHACK", "New Intent | Action: " + intent.getAction());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
