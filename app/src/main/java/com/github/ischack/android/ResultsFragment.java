package com.github.ischack.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.github.ischack.android.model.Game;
import com.github.ischack.android.model.GameScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 6/28/14.
 */
public class ResultsFragment extends ListFragment {

    List<GameScore> scores;

    ListAdapter mAdapter;

    public static ResultsFragment newInstance(ArrayList<GameScore> scores) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("scores", scores);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scores = getArguments().getParcelableArrayList("scores");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ArrayAdapter<GameScore>(getActivity(), android.R.layout.simple_list_item_1, scores);
        setListAdapter(mAdapter);
    }

}
