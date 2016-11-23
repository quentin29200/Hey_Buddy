package com.example.quentin.heybuddy;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "Creating the menu view ...");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_options, container, false);
    }
}