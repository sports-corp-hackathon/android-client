package com.github.ischack.android;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.ischack.android.fragments.gamefragment.GameFragment;
import com.github.ischack.android.helpers.NfcUtils;
import com.github.ischack.android.model.Game;

public class GameActivity extends FragmentActivity {

    Game game;
    GameFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = getIntent().getExtras().getParcelable("game");

        gameFragment = GameFragment.newInstance(game);
        getSupportFragmentManager().beginTransaction().add(R.id.content, gameFragment, GameFragment.class.getName()).commit();

        getActionBar().setTitle(game.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ISCHACK", "Action: " + getIntent().getAction());

        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey(NfcAdapter.EXTRA_ID)) {
            Log.d("NFC", NfcUtils.ByteArrayToHexString(bundle.getByteArray(NfcAdapter.EXTRA_ID)));

            gameFragment.promptScore( NfcUtils.ByteArrayToHexString(bundle.getByteArray(NfcAdapter.EXTRA_ID)));
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
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter},  NfcUtils.techList);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(i.getAction())) {
            Toast.makeText(this,  NfcUtils.ByteArrayToHexString(i.getByteArrayExtra(NfcAdapter.EXTRA_ID)), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
