package com.example.quentin.heybuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;

import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.os.Bundle;
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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static long MIN_TIME_UPDATE = 60000;
    private static long MIN_DISTANCE_UPDATES = 150;
    final private static int ALLOW_APP_GPS = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Initialization of the root layer
        LinearLayout llLayout = (LinearLayout) inflater.inflate(R.layout.activity_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


        try {
            LocationManager locationManager = (LocationManager) super.getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (ContextCompat.checkSelfPermission(super.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(super.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // TODO Is it normal to have an empty body here?
                } else {
                    ActivityCompat.requestPermissions(super.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ALLOW_APP_GPS);
                }
            }
            // Get GPS and network status
            Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled)    {
                Log.e("MapsActivity", "GPS pas activé" );
                // cannot get location
                AlertDialog.Builder dialog = new AlertDialog.Builder(super.getActivity());
                dialog.setMessage("Veuillez activer votre GPS");
                dialog.setPositiveButton("Lancer GPS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        //get gps
                    }
                });
                dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Is it normal to have an empty body here?
                    }
                });
                dialog.show();
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATES, new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

                @Override
                public void onLocationChanged(Location location) {
                    LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                    mMap.moveCamera(CameraUpdateFactory.zoomBy(13));
                }
            });
            mMap.setMyLocationEnabled(true);
            /*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            mMap.moveCamera(CameraUpdateFactory.zoomBy(13));
            Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
*/
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALLOW_APP_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("MapsActivity", "GPS autorisé" );
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e("MapsActivity", "GPS pas autorisé" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                // return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ALLOW_APP_GPS) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }*/
}
