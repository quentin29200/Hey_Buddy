package fr.istic.m2miage.heybuddy.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.fragments.MapFragment;
import fr.istic.m2miage.heybuddy.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity {

    /** TAG for logs, corresponding to the Activity name */
    private static final String TAG = "MainActivity";

    /**
     * Check the documentation here
     * https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle)
     *
     * @param savedInstanceState - {Bundle} If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Log
        Log.v(TAG, "Initialization processing ...");

        // Get the FragmentManager to proceed to the transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
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
            /**
             * Check the example here
             * http://stackoverflow.com/questions/26300674/android-fragment-overlay-another-fragment-with-semi-transparent
             */

            // Log
            Log.v(TAG, "Smartphone configuration detected!");

            // BACKGROUND - Get the map as a fragment
            MapFragment map = new MapFragment();

            // FOREGROUND - Get the menu as a fragment
            MenuFragment menu = new MenuFragment();

            // Add the fragment to the container
            fragmentTransaction.add(R.id.fragmentLayoutBackground, map).commit();
            fragmentTransaction.add(R.id.fragmentLayoutForeground, menu).commit();
        }

        // Confirm the transaction
        fragmentTransaction.commit();

        // Log
        Log.v(TAG, "Process initialized successfully!");
    }

}
