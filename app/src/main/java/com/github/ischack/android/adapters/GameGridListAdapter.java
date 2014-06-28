package com.github.ischack.android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ischack.android.R;
import com.github.ischack.android.model.Game;

/**
 * Created by david on 6/27/14 for android
 */
public class GameGridListAdapter extends ArrayAdapter<Game> {

    private LayoutInflater inflater;

    public GameGridListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Game.ViewHolder viewHolder;

        if(convertView != null) {
            v = convertView;
            viewHolder = (Game.ViewHolder) v.getTag();
        } else {
            v = getInflater().inflate(R.layout.game_grid_item, parent, false);
            viewHolder = new Game.ViewHolder((ImageView) v.findViewById(R.id.gameImage), (TextView) v.findViewById(R.id.gameName));
            v.setTag(viewHolder);
        }

        TextView name = viewHolder.getTextView();

        name.setText(getItem(position).getName());

        ImageView iv = viewHolder.getImageView();

        iv.setImageBitmap(getItem(position).getImage());

        return v;
    }

    public LayoutInflater getInflater() {
        if(inflater == null)
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater;
    }

    @Override
    synchronized public void add(Game g)
    {
        super.add(g);
    }
}
