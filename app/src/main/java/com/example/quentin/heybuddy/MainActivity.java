package com.example.quentin.heybuddy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Log
        Log.v(TAG, "Initialization processing ...");

        // Get the FragmentManager to proceed to the transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Tablet configuration
        if (findViewById(R.id.layoutTablet) != null)
        {
            // Log
            Log.v(TAG, "Tablet configuration detected!");

            // LEFT SIDE - Get the menu as a fragment
            MenuFragment menu = new MenuFragment();

            // RIGHT SIDE - Get the map as a fragment
            MapFragment map = new MapFragment();

            // Add the fragment to the container
            fragmentTransaction.add(R.id.fragmentLayoutLeft, menu);
            fragmentTransaction.add(R.id.fragmentLayoutRight, map);

        }
        // Casual smartphone configuration
        else
        {
            // Log
            Log.v(TAG, "Smartphone configuration detected!");

            // BACKGROUND - The Google Map
            MapFragment map = new MapFragment();

            // TODO Finish this part
        }

        // Confirm the transaction
        fragmentTransaction.commit();

        // Log
        Log.v(TAG, "Process initialized successfully!");
    }

}
