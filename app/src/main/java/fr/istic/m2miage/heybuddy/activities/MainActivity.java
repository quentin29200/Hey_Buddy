package fr.istic.m2miage.heybuddy.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.adapter.ContactAdapter;
import fr.istic.m2miage.heybuddy.firebase.FirebaseInstanceIdManager;
import fr.istic.m2miage.heybuddy.firebase.User;
import fr.istic.m2miage.heybuddy.fragments.MapFragment;

/**
 * Done with the tutorial
 * https://github.com/codepath/android_guides/wiki/Fragment-navigation-drawer
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.lstPhoneContact) ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private ContactAdapter contactAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Check token in shared preference to store in firebase
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(FirebaseInstanceIdManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(FirebaseInstanceIdManager.TAG_TOKEN, null);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(token != null && user != null){
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
            sharedPreferences.edit().remove("token").apply();
        }

        mTitle = mDrawerTitle = getTitle();

        this.contactAdapter = new ContactAdapter(this);
        this.mDrawerList.setAdapter(this.contactAdapter);
        this.mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Highlight the selected item, update the title, and close the drawer
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);

        // Add the MapFragment
        Class fragmentClass = MapFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}