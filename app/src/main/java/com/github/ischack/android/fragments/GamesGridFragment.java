package com.github.ischack.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.model.Game;
import com.github.ischack.android.tasks.GetGameImageTask;

import java.util.ArrayList;

/**
 *
 */
public class GamesGridFragment extends Fragment {

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



        new GetGameImageTask(mAdapter, getActivity()).execute(new ArrayList<Game>());

    }
}
