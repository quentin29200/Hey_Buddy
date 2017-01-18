package fr.istic.m2miage.heybuddy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.firebase.User;

/**
 * Created by mahdi on 01/12/16.
 */

public class ContactAdapter extends BaseAdapter {

    private List<Contact> contactList;
    private Activity activity;
    private class Contact {

        private long id;
        private String name;
        private String image;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }


    public ContactAdapter(Activity activity) {
        this.activity = activity;
        this.contactList = new ArrayList<>();
        initPhoneContactList();
    }

    @Override
    public int getCount() {
        return this.contactList.size();
    }

    @Override
    public Contact getItem(int i) {
        return this.contactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.contactList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View item =  inflater.inflate(R.layout.contact_list_item,null,false);

        TextView txtContactName   = (TextView) item.findViewById(R.id.txtContactName);
        Contact contact = this.contactList.get(i);
        txtContactName.setText(contact.getName());

        return item;
    }

    private void initPhoneContactList() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            // GET USERS UID
            ref.child("friends").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot friendSnapshot: dataSnapshot.getChildren()){
                        // Get Friend UID
                        String friendUid = friendSnapshot.getValue(String.class);

                        // Get Friend OBJECT
                        ref.child("users").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                Contact contact = new Contact();
                                contact.setName(user.getUsername());
                                contactList.add(contact);
                                notifyDataSetChanged();
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
