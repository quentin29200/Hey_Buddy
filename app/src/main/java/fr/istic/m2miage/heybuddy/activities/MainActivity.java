package fr.istic.m2miage.heybuddy.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.adapter.ContactAdapter;
import fr.istic.m2miage.heybuddy.fragments.MapFragment;

/**
 * Done with the tutorial
 * https://github.com/codepath/android_guides/wiki/Fragment-navigation-drawer
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.lstPhoneContact) ListView mDrawerList;
    @BindView(R.id.floatLocation) FloatingActionButton floatLocation;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        // Add the MapFragment
        final Class fragmentClass = MapFragment.class;
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "map").commit();

        this.floatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("map");
                mapFragment.showMe();
            }
        });
    }
}