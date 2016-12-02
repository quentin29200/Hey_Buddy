package fr.istic.m2miage.heybuddy.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    public static String getUserEmail(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser.getEmail();
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

    /**
     * COPIER LE CODE DE CETTE METHODE LA OU IL Y EN A BESOIN
     */
    public static void getUserFriends(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            // GET USERS UID
            ref.child("friends").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<String> friendsUID = new ArrayList<>();
                    final List<User> friendsUsers = new ArrayList<>();
                    final List<Position> friendsPositions = new ArrayList<>();
                    for(DataSnapshot friendSnapshot: dataSnapshot.getChildren()){
                        String uid = friendSnapshot.getValue(String.class);
                        friendsUID.add(uid);

                        // GET USERS OBJECT
                        ref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                friendsUsers.add(user);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        // GET USERS POSITIONS
                        ref.child("positions").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Position pos = dataSnapshot.getValue(Position.class);
                                friendsPositions.add(pos);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
