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
            setUserUsername(user.getUsername());
        }
    }

    public static String getUserUsername(){
        String username = null;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            username = ref.child("usernames").child(firebaseUser.getUid()).getKey();
        }
        return username;
    }

    public static void setUserUsername(String username){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("usernames").child(firebaseUser.getUid()).setValue(username);
        }
    }

    public static void setUserPosition(float latitude, float longitude){
        Position position = new Position(latitude, longitude);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("positions").child(firebaseUser.getUid()).setValue(position);
        }
    }

    /**
     * Permet de récupérer la position d'un utilisateur
     * @param uid identifiant de l'utilisateur concerné
     * @return L'objet position associé
     */
    public static Position getUserPosition(String uid){
        return null;
    }
}
