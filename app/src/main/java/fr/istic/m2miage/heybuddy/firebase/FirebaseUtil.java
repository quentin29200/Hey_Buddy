package fr.istic.m2miage.heybuddy.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Classe utilitaire qui permet de manipuler la base de données Firebase
 */
public class FirebaseUtil {

    /**
     * Ajoute un utilisateur dans la base
     * @param user Objet correspondant à un utilisateur
     */
    public static void addUser(User user){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(firebaseUser.getUid()).setValue(user);
            setUserUsername(user.getUsername());
        }
    }

    /**
     * Change le nom d'un utilisateur
     * @param username Le nouveau nom à affecter
     */
    public static void setUserUsername(String username){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("usernames").child(firebaseUser.getUid()).setValue(username);
        }
    }

    /**
     * Modifie l'emplacement GPS de l'utisateur dans la base
     * @param latitude Latitude de la position GPS
     * @param longitude Longitude de la position GPS
     */
    public static void setUserPosition(double latitude, double longitude){
        Position position = new Position(latitude, longitude);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("positions").child(firebaseUser.getUid()).setValue(position);
        }
    }

    /**
     * Ajoute un utilisateur en tant qu'ami de l'utilisateur courant
     * @param uid Identifiant de l'utilisateur à ajouter
     */
    public static void addUserFriend(String uid){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("friends").child(firebaseUser.getUid()).push().setValue(uid);
        }
    }
}
