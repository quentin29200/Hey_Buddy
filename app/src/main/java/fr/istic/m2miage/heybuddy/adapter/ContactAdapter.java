package fr.istic.m2miage.heybuddy.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.m2miage.heybuddy.R;

/**
 * Created by mahdi on 01/12/16.
 */

public class ContactAdapter extends BaseAdapter {

    private List<Contact> contactList;
    private Activity activity;
    private class Contact {
        private long id;
        private String name;
        private String lastName;
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

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
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
        TextView txtContactSurname = (TextView) item.findViewById(R.id.txtContactName);
        ImageView imgContact = (ImageView) item.findViewById(R.id.imgContact);

        Contact contact = this.contactList.get(i);

        txtContactName.setText(contact.getName());
        txtContactSurname.setText(contact.getLastName());
        imgContact.setImageURI(Uri.parse(contact.getImage()));

        return item;
    }

    private void initPhoneContactList() {
        ContentResolver contentResolver = this.activity.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                Contact newContact = new Contact();
                newContact.setId(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                newContact.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                newContact.setLastName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID)) > 0) {
                    newContact.setImage(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
                }
                else {
                    newContact.setImage("");
                }
                this.contactList.add(newContact);
            }
            cursor.close();
        }
    }
}
