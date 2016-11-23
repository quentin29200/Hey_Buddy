package com.example.quentin.heybuddy;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    /**
     * Check the documentation here
     * https://developer.android.com/reference/android/app/Fragment.html#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     *
     * @param inflater - {LayoutInflater} The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container - {ViewGroup} If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState - {Bundle} If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return View	- Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "Creating the menu view ...");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_options, container, false);
    }
}