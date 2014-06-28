package com.github.ischack.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ischack.android.R;
import com.github.ischack.android.fragments.gamefragment.InfoFragment;
import com.github.ischack.android.fragments.gamefragment.LeaderboardFragment;
import com.github.ischack.android.fragments.gamefragment.RulesFragment;
import com.github.ischack.android.model.Game;

public class FragmentTabsFragmentSupport extends Fragment {
    private FragmentTabHost mTabHost;

    private Game game;

    // TODO: Rename and change types and number of parameters
    public static FragmentTabsFragmentSupport newInstance(Game game) {
        FragmentTabsFragmentSupport fragment = new FragmentTabsFragmentSupport();
        Bundle args = new Bundle();
        args.putParcelable("game", game);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTabsFragmentSupport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            game = getArguments().getParcelable("game");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

        Bundle args = new Bundle();
        args.putParcelable("game", game);

        mTabHost.addTab(mTabHost.newTabSpec("leaders").setIndicator("Leaders"),
                LeaderboardFragment.class, args);
        mTabHost.addTab(mTabHost.newTabSpec("rules").setIndicator("Rules"),
                RulesFragment.class, args);
        mTabHost.addTab(mTabHost.newTabSpec("info").setIndicator("Info"),
                InfoFragment.class, args);

        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}