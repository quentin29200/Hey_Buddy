package fr.istic.m2miage.heybuddy.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import fr.istic.m2miage.heybuddy.activities.MainActivity;
import fr.istic.m2miage.heybuddy.firebase.Contact;
import fr.istic.m2miage.heybuddy.firebase.User;
import fr.istic.m2miage.heybuddy.fragments.MapFragment;

/**
 * Created by mahdi on 01/12/16.
 */

public class ContactAdapter extends BaseAdapter {

    private List<Contact> contactList;
    private MainActivity activity;
    //private MapFragment mapFragment;

    public ContactAdapter(Activity activity) {
        this.activity = (MainActivity) activity;
        this.contactList = new ArrayList<>();
       // Log.i("MapFragment Contact", mapFragment.getClass().toString());
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
        final Contact contact = this.contactList.get(i);
        LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View item =  inflater.inflate(R.layout.contact_list_item,null,false);
        TextView txtContactName   = (TextView) item.findViewById(R.id.txtContactName);
        ImageView imgShowFriend = (ImageView) item.findViewById(R.id.showThisFriend);
        imgShowFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapFragment mapFragment = (MapFragment) activity.getSupportFragmentManager().findFragmentByTag("map");
                activity.getmDrawerLayout().closeDrawer(activity.getNavigationView());
                mapFragment.showFriendOnMap(contact);
            }
        });
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
                        final String friendUid = friendSnapshot.getValue(String.class);

                        // Get Friend OBJECT
                        ref.child("users").child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                Contact contact = new Contact();
                                contact.setName(user.getUsername());
                                contact.setUid(friendUid);
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
