package fr.istic.m2miage.heybuddy.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mahdi on 18/01/17.
 */

public class FirebaseInstanceIdManager extends FirebaseInstanceIdService {

    private final String TAG_TOKEN = "tagToken";
    private final String SHARED_PREF_NAME = "FMSPref";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Refreshed token " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
    }
}
