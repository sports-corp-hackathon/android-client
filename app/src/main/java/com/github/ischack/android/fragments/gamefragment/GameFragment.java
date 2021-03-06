package com.github.ischack.android.fragments.gamefragment;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.github.ischack.android.R;
import com.github.ischack.android.fragments.FragmentTabsFragmentSupport;
import com.github.ischack.android.helpers.GameDownloadHelper;
import com.github.ischack.android.model.Game;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GameFragment extends Fragment {

    private static final String GAME_KEY = "Game";

    // TODO: Rename and change types of parameters
    private Game game;

    private boolean promptingScore = false;
    private String currentUserId;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param game Parameter game.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(Game game) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putParcelable(GAME_KEY, game);
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = getArguments().getParcelable(GAME_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ISCHACK", "Inflating the game view...");
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        ((TextView) v.findViewById(R.id.title)).setText(game.getName());

        final ImageView iv = ((ImageView) v.findViewById(R.id.gameImage));

        new AsyncTask<Game, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Game... params) {
                Bitmap bm = GameDownloadHelper.downloadImageForGame(getActivity(), params[0]);

                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                iv.setImageBitmap(bitmap);
            }
        }.execute(game);

        return v;
    }



    public void promptScore(String userId) {
        this.currentUserId = userId;
        if(!promptingScore) {
            for (Fragment f : getActivity().getSupportFragmentManager().getFragments()) {
                if (f instanceof DialogFragment)
                    return;
            }
            showDialogFragmentForGame(game);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment frag = FragmentTabsFragmentSupport.newInstance(game);
        getChildFragmentManager().beginTransaction().add(R.id.content, frag, FragmentTabsFragmentSupport.class.getName()).commit();
    }

    interface onPublishScoreCallback {
        public void onSuccess();
        public void onFail(int code);
    }

    private void publishScoreNonBlocking(final String score, final onPublishScoreCallback callback) {

        new AsyncTask<Game, Void, Integer>() {

            @Override
            protected Integer doInBackground(Game... params) {

                HttpPost httpPost = new HttpPost("http://devnull-as-a-service.com/dev/null");

                JSONObject jo = new JSONObject();

                try {
                    jo.put("playerId", currentUserId);
                    jo.put("score", score);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    httpPost.setEntity(new StringEntity(jo.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpClient client = new DefaultHttpClient();

                HttpResponse response = null;
                try {
                    response = client.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(response != null) {
                    StatusLine line = response.getStatusLine();
                    Log.i("ISCHACK", "complete: " + line);
                    // return code indicates upload failed so throw exception
                    return line.getStatusCode();
                }

                return -1;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if( callback != null) {
                    if (integer < 200 || integer > 299)
                        callback.onFail(integer);
                    else
                        callback.onSuccess();
                }
            }
        }.execute(game);
    }

    public void showDialogFragmentForGame(Game game)
    {
        Game.ScoreType type = game.getScoreType();

        switch(type) {
            case TIME:
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setFragmentManager(getActivity().getSupportFragmentManager())
                        .addHmsPickerDialogHandler(new HmsPickerDialogFragment.HmsPickerDialogHandler() {
                            @Override
                            public void onDialogHmsSet(int i, int i2, int i3, int i4) {
                                publishScoreNonBlocking("" + i + " " + i2 + " " + i3 + " " + i4, null);
                            }
                        })
                        .setStyleResId(R.style.BetterPickersDialogFragment);

                hpb.show();

                break;
            case RANK:
                promptingScore = true;
                final Dialog d2 = new Dialog(getActivity());
                d2.setTitle("Enter score for this player");
                d2.setContentView(R.layout.picker_count);
                Button b1 = (Button) d2.findViewById(R.id.button1);
                Button b2 = (Button) d2.findViewById(R.id.button2);
                final NumberPicker np = (NumberPicker) d2.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(0);
                np.setWrapSelectorWheel(false);
                //np.setOnValueChangedListener(this);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        //TODO: Send score.
                        publishScoreNonBlocking(Integer.toString(np.getValue()), null);
                        promptingScore = false;
                        d2.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        promptingScore = false;
                        d2.dismiss();
                    }
                });
                d2.show();
                break;
            case BOOL:
                promptingScore = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Did this player complete the game?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                publishScoreNonBlocking("yes", null);

                                promptingScore = false;
                                // FIRE ZE MISSILES!
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                publishScoreNonBlocking("no", null);

                                promptingScore = false;
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                break;
            case DISTANCE:
                promptingScore = true;
                final Dialog d = new Dialog(getActivity());
                d.setTitle("Enter distance for this player");
                d.setContentView(R.layout.picker_distance);
                Button _b1 = (Button) d.findViewById(R.id.button1);
                Button _b2 = (Button) d.findViewById(R.id.button2);
                final NumberPicker npin = (NumberPicker) d.findViewById(R.id.numberPicker_in);
                npin.setMaxValue(11);
                npin.setMinValue(0);
                npin.setWrapSelectorWheel(false);

                final NumberPicker npft = (NumberPicker) d.findViewById(R.id.numberPicker_ft);
                npft.setMaxValue(100);
                npft.setMinValue(0);
                npft.setWrapSelectorWheel(false);

                //np.setOnValueChangedListener(this);
                _b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        publishScoreNonBlocking(npft + "ft" + npin.getValue() + "in", null);
                        promptingScore = false;
                        d.dismiss();
                    }
                });
                _b2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        promptingScore = false;
                        d.dismiss();
                    }
                });
                d.show();
                break;

            case COUNT:
                promptingScore = true;
                final Dialog d3 = new Dialog(getActivity());
                d3.setTitle("Enter score for this player");
                d3.setContentView(R.layout.picker_count);
                Button _b3 = (Button) d3.findViewById(R.id.button1);
                Button _b4 = (Button) d3.findViewById(R.id.button2);
                final NumberPicker _np = (NumberPicker) d3.findViewById(R.id.numberPicker1);
                _np.setMaxValue(100);
                _np.setMinValue(0);
                _np.setWrapSelectorWheel(false);
                //np.setOnValueChangedListener(this);
                _b3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        publishScoreNonBlocking(Integer.toString(_np.getValue()), null);
                        promptingScore = false;
                        d3.dismiss();
                    }
                });
                _b4.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        promptingScore = false;
                        d3.dismiss();
                    }
                });
                d3.show();
                break;
        }
    }
}
