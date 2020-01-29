package com.business.order_trip;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProfilePressActivity extends AppCompatActivity {

    private final int PICK_CONTACT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_press);

        ImageButton ibback = (ImageButton) findViewById(R.id.ib_back);
        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ImageButton button = (ImageButton) findViewById(R.id.ib_contacts);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//                String aNameFromContacts[] = new String[contacts.getCount()];
//                String aNumberFromContacts[] = new String[contacts.getCount()];
//                int i = 0;
//
//                int nameFieldColumnIndex = contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//                int numberFieldColumnIndex = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//
//                while(contacts.moveToNext()) {
//
//                    String contactName = contacts.getString(nameFieldColumnIndex);
//                    aNameFromContacts[i] =    contactName ;
//
//                    String number = contacts.getString(numberFieldColumnIndex);
//                    aNumberFromContacts[i] =    number ;
//                    i++;
//                }
//
//                contacts.close();
//            }
//        });

    }
}
