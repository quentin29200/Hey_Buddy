package fr.istic.m2miage.heybuddy.firebase;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * Ajout des contacts du répertoire qui sont inscrits sur HeyBuddy
     * @param activity L'activité qui appelle la méthode.
     */
    public static void addFriendsFromContacts(Activity activity){
        ContentResolver contentResolver = activity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        class Contact{
            private long id;
            private String name;
            private String lastName;
            private String image;
            private String numero;
        }

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {

                // GET CONTACT DATA
                final Contact newContact = new Contact();
                newContact.id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                newContact.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                newContact.lastName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)) > 0) {
                    newContact.image = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                }
                else {
                    newContact.image = "";
                }

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[]{cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))}, null);
                    while (phoneCursor.moveToNext()) {
                        newContact.numero = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phoneCursor.close();
                }


                // AJOUT A FIREBASE SI EXISTE
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                    ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                                final String uid = userSnapshot.getKey();
                                User user = userSnapshot.getValue(User.class);
                                if(user.getNumero() != null && PhoneNumberUtils.compare(user.getNumero(), newContact.numero)){
                                    ref.child("friends").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Map<String, String> friend = (Map<String, String>) dataSnapshot.getValue();
                                            if(friend != null && !friend.containsValue(uid)){
                                                ref.child("friends").child(firebaseUser.getUid()).push().setValue(uid);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            cursor.close();
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
