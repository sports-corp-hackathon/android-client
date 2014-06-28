package com.github.ischack.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.HeaderViewListAdapter;
import com.etsy.android.grid.StaggeredGridView;
import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.model.Game;
import com.github.ischack.android.tasks.GetGameImageTask;

import java.util.ArrayList;

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

        new GetGameImageTask(mAdapter, getActivity()).execute(new ArrayList<Game>());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Game game = (Game) view.getTag();

        Toast.makeText(getActivity(), "Clicked: " + game.getName(), Toast.LENGTH_LONG).show();

        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(GameFragment.class.getName()).replace(android.R.id.content, GameFragment.newInstance(game), GameFragment.class.getName()).commit();
    }
}
