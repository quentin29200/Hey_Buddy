package fr.istic.m2miage.heybuddy.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mahdi on 18/01/17.
 */

public class FirebaseInstanceIdManager extends FirebaseInstanceIdService {

    public static final String TAG_TOKEN = "tagToken";
    public static final String SHARED_PREF_NAME = "FMSPref";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", "Refreshed token " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(final String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String uid = dataSnapshot.getKey();
                    User user = dataSnapshot.getValue(User.class);
                    user.setToken(token);
                    ref.child("users").child(uid).setValue(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TAG_TOKEN, token);
            editor.apply();
        }
    }
}
