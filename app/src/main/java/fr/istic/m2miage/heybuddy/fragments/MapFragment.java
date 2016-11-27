package fr.istic.m2miage.heybuddy.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import fr.istic.m2miage.heybuddy.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static long MIN_TIME_UPDATE = 60000;
    private static long MIN_DISTANCE_UPDATES = 150;
    final private static int ALLOW_APP_GPS = 0;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Initialization of the root layer
        LinearLayout llLayout = (LinearLayout) inflater.inflate(R.layout.activity_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Must return the root layer
        return llLayout;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Check documentation here
     * https://developers.google.com/android/reference/com/google/android/gms/maps/OnMapReadyCallback.html#onMapReady(com.google.android.gms.maps.GoogleMap)
     *
     * @param googleMap - {GoogleMap} A non-null instance of a GoogleMap associated with the MapFragment or MapView that defines the callback.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            LocationManager locationManager = (LocationManager) super.getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ContextCompat.checkSelfPermission(super.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(super.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // TODO: Add the code here
                } else {
                    ActivityCompat.requestPermissions(super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ALLOW_APP_GPS);
                }
            }
            // Get GPS and network status
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled) {
                Log.e("MapsActivity", "GPS pas activé" );
                // cannot get location
                AlertDialog.Builder dialog = new AlertDialog.Builder(super.getActivity());
                dialog.setMessage(R.string.pls_accept_GPS);
                dialog.setPositiveButton(R.string.run_GPS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO: Add the code here
                    }
                });
                dialog.show();
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATES, new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // TODO: Add the code here
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // TODO: Add the code here
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO: Add the code here
                }

                @Override
                public void onLocationChanged(Location location) {
                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    mMap.moveCamera(CameraUpdateFactory.zoomBy(13));
                }
            });
            mMap.setMyLocationEnabled(true);
        } catch (Exception ex) {
            Log.i("MapsActivity", "Error creating location service: " + ex.getMessage());
        }
    }

    /**
     * Check documentation here
     * https://developer.android.com/reference/android/support/v4/app/ActivityCompat.OnRequestPermissionsResultCallback.html
     *
     * @param requestCode - {int} The request code passed in requestPermissions(android.app.Activity, String[], int)
     * @param permissions - {String} The requested permissions. Never null.
     * @param grantResults - {int} The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALLOW_APP_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MapsActivity", "GPS autorisé" );
                } else {
                    Log.e("MapsActivity", "GPS pas autorisé" );
                }
            }
        }
    }
}