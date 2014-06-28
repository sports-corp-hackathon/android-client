package com.github.ischack.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.ischack.android.MainActivity;
import com.github.ischack.android.R;
import com.github.ischack.android.adapters.GameGridListAdapter;
import com.github.ischack.android.helpers.EmailTextWatcher;
import com.github.ischack.android.model.Game;

/**
 * Created by david on 6/27/14 for android
 */
public class VolunteerLoginFragment extends Fragment {

    EditText emailBox;
    Button regButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public VolunteerLoginFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailBox = (EditText) view.findViewById(R.id.etxt_email);
        regButton = (Button) view.findViewById(R.id.btn_register);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit().putString("user_type", "volunteer").commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new GamesGridFragment(), GamesGridFragment.class.getName()).commit();
            }
        });

        emailBox.addTextChangedListener(new EmailTextWatcher(getActivity(), emailBox, regButton));

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
