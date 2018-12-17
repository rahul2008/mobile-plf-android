package com.philips.platform.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class AddAddressActivity extends Activity implements OnClickListener {

    SecureDataBaseQueryHelper secureDataBaseQueryHelper;

    private EditText firstname_et, lastname_et, address_et, contact_et;
    private Button reset_btn, submit_btn, bulk_insert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        secureDataBaseQueryHelper = MainActivity.getSecureDataBaseQueryHelper();


        firstname_et = (EditText) findViewById(R.id.etFirstName);
        lastname_et = (EditText) findViewById(R.id.etLasttName);
        address_et = (EditText) findViewById(R.id.etAddress);
        contact_et = (EditText) findViewById(R.id.etMobileNumber);


        reset_btn = (Button) findViewById(R.id.reset_btn);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        bulk_insert = (Button) findViewById(R.id.bulk_insert);


        reset_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        bulk_insert.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.submit_btn:

                if (firstname_et.getText().toString().length() > 0 || lastname_et.getText().toString().length() > 0 || address_et.getText().toString().length() > 0 || contact_et.getText().toString().length() > 0) {
                    AddressBook addressBook = new AddressBook();
                    addressBook.firstName = firstname_et.getText().toString();
                    addressBook.lastName = lastname_et.getText().toString();
                    addressBook.address = address_et.getText().toString();
                    addressBook.contactNumber = contact_et.getText().toString();
                    secureDataBaseQueryHelper.createOrInsert(AddressBook.class, addressBook);
                    reset();
                    showDialog();
                } else {
                    Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.reset_btn:
                reset();
                break;

            case R.id.bulk_insert:
                secureDataBaseQueryHelper.bulkInsert();
                break;
        }


    }


    // Clear the entered text
    private void reset() {
        firstname_et.setText(null);
        lastname_et.setText(null);
        address_et.setText(null);
        contact_et.setText(null);
        ;
    }

    private void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Address record added successfully !!");

        alertDialogBuilder.setPositiveButton("Add More",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("View All Records",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewAllRecords();
                        finish();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void viewAllRecords() {
        final Intent negativeActivity = new Intent(getApplicationContext(), ViewAddressRecordActivity.class);
        List<AddressBook> addressList = secureDataBaseQueryHelper.retrieveAll(AddressBook.class);

        negativeActivity.putExtra("ADDREES_BOOK", (Serializable) addressList);
        startActivity(negativeActivity);
    }

}
