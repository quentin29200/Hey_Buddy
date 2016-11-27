package fr.istic.m2miage.heybuddy.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Florian on 25/11/2016.
 */

public class FirebaseUtil {

    public static void addUser(User user){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(firebaseUser.getUid()).setValue(user);
//            addUserEmail(user);
//            addUserUsername(user);
        }
    }

//    public static void addUserEmail(User user){
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null){
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//            ref.child("emails").child(user.getEmail()).setValue(firebaseUser.getUid());
//        }
//    }

    public static void addUserUsername(User user){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("usernames").child(user.getUsername()).setValue(firebaseUser.getUid());
        }
    }

    public static void setUserPosition(String position){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("positions").child(firebaseUser.getUid()).setValue(position);
        }
    }
}
