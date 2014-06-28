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

public class FragmentTabsFragmentSupport extends Fragment {
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Leaders"),
                LeaderboardFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("Rules"),
                RulesFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Info"),
                InfoFragment.class, null);

        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}