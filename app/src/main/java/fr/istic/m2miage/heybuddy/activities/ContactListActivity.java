package fr.istic.m2miage.heybuddy.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import fr.istic.m2miage.heybuddy.R;
import fr.istic.m2miage.heybuddy.adapter.ContactAdapter;

public class ContactListActivity extends AppCompatActivity {

    private ListView lstPhoneContacts;
    private ContactAdapter contactAdapter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            this.contactAdapter = new ContactAdapter(this);
            this.lstPhoneContacts = (ListView) findViewById(R.id.lstPhoneContact);
            this.lstPhoneContacts.setAdapter(this.contactAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                this.contactAdapter = new ContactAdapter(this);
                this.lstPhoneContacts = (ListView) findViewById(R.id.lstPhoneContact);
                this.lstPhoneContacts.setAdapter(this.contactAdapter);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
