package com.philips.platform.database;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.securedblibrary.ormlite.sqlcipher.android.apptools.OpenHelperManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnClickListener {

    private static final String DATABASE_NAME = "address.db";
    public static String DATABASE_PASSWORD_KEY = "hi";
    static SecureDataBaseQueryHelper secureDataBaseQueryHelper;
    static SecureStorageInterface mSecureStorage = null;
    static String UPDATE = "update";
    static String UPDATE_ALL = "updateall";
    static String DELETE = "delete";
    static String GET = "getById";
    private static int DATABASE_VERSION = 3;
    SecureDataBaseHelper secureDataBaseHelper;
    private Button add_btn, del_btn, del_all_btn, view_btn, view_All_btn, update_all_btn, update_btn;

    public static SecureDataBaseQueryHelper getSecureDataBaseQueryHelper() {
        return secureDataBaseQueryHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secureDataBaseHelper = new SecureDataBaseHelper<>(this,SecureDBApplication.getAppInfraInterface() ,AddressBook.class, DATABASE_NAME, DATABASE_VERSION, DATABASE_PASSWORD_KEY);
        secureDataBaseQueryHelper = new SecureDataBaseQueryHelper(this, secureDataBaseHelper, "hi");

        add_btn = (Button) findViewById(R.id.createOrInsert_btn);
        del_btn = (Button) findViewById(R.id.deleteById_btn);
        del_all_btn = (Button) findViewById(R.id.deleteAll_btn);
        view_btn = (Button) findViewById(R.id.retrieveById_btn);
        view_All_btn = (Button) findViewById(R.id.retrieveAll_btn);
        update_all_btn = (Button) findViewById(R.id.updateAll_btn);
        update_btn = (Button) findViewById(R.id.updateById_btn);
        TextView version=(TextView) findViewById(R.id.appversion);
        version.setText(secureDataBaseHelper.getAppVersion());

        // Attachment of onClickListner for them
        add_btn.setOnClickListener(this);
        del_btn.setOnClickListener(this);
        del_all_btn.setOnClickListener(this);
        view_btn.setOnClickListener(this);
        view_All_btn.setOnClickListener(this);
        update_all_btn.setOnClickListener(this);
        update_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.createOrInsert_btn:
                startActivity(new Intent(getApplicationContext(), AddAddressActivity.class));
                break;
            case R.id.deleteAll_btn:
                deleteAllRecords();
                break;
            case R.id.deleteById_btn:
                openDialog(DELETE);
                break;
            case R.id.retrieveById_btn:
                openDialog(GET);
                break;
            case R.id.retrieveAll_btn:
                viewAllRecords();
                break;
            case R.id.updateAll_btn:
                updateAllRecords();
                break;
            case R.id.updateById_btn:
                openDialog(UPDATE);
                break;
        }
    }

    private void viewAllRecords() {
        final Intent negativeActivity = new Intent(getApplicationContext(), ViewAddressRecordActivity.class);
        List<AddressBook> addressList = secureDataBaseQueryHelper.retrieveAll(AddressBook.class);

        negativeActivity.putExtra("ADDREES_BOOK", (Serializable) addressList);
        startActivity(negativeActivity);
    }

    private void viewByIdRecords(int id) {
        final Intent negativeActivity = new Intent(getApplicationContext(), ViewAddressRecordActivity.class);

        AddressBook address = (AddressBook) secureDataBaseQueryHelper.retrieveById(AddressBook.class, id);


        List<AddressBook> addressList = new ArrayList<AddressBook>();
        if (address == null) {
            addressList.clear();
        } else {
            addressList.add(address);
        }
        negativeActivity.putExtra("ADDREES_BOOK", (Serializable) addressList);
        startActivity(negativeActivity);
    }

    private void deleteAllRecords() {
        int deleteRecordCount = secureDataBaseQueryHelper.deleteAll(AddressBook.class);

        if (deleteRecordCount > 0) {
            showMessageDialog("Successfully All records deleted");
        }
        Log.i("", "deleteAllRecords" + deleteRecordCount);


    }

    private void deleteByIdRecords(int id) {
        int deleteRecordCount = secureDataBaseQueryHelper.deleteById(AddressBook.class, id);
        if (deleteRecordCount > 0) {
            showMessageDialog("Successfully deleted" + deleteRecordCount + " records");
        }
        Log.i("", "deleteById" + deleteRecordCount);

    }

    private <T> void updateAllRecords() {

        int updateRecordCount = secureDataBaseQueryHelper.updateAllRecords(AddressBook.class, "address", "Bangalore");

        if (updateRecordCount > 0) {
            showMessageDialog("Successfully All records Updated");
        }
        Log.i("MainActivity", "updateRecordCount" + updateRecordCount);


    }

    private void updateByWhere(int id) {
        int updateRecordCount = secureDataBaseQueryHelper.updateRecordByWhere(AddressBook.class, "Bangalore", "address_id", id, "address");
        if (updateRecordCount > 0) {
            showMessageDialog("Successfully Updated" + updateRecordCount + " records");
        }
        Log.i("MainActivity", "updateById" + updateRecordCount);

    }

    private void showMessageDialog(final String message) {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openDialog(final String type) {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.alert_dialog, null);
        final EditText subEditText = (EditText) subView.findViewById(R.id.dialogEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (type.equalsIgnoreCase(GET)) {
                    viewByIdRecords(Integer.parseInt(subEditText.getText().toString()));

                } else if (type.equalsIgnoreCase(UPDATE)) {
                    updateByWhere(Integer.parseInt(subEditText.getText().toString()));
                } else if (type.equalsIgnoreCase(DELETE)) {
                    deleteByIdRecords(Integer.parseInt(subEditText.getText().toString()));
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        secureDataBaseQueryHelper.close();
        secureDataBaseQueryHelper = null;
        secureDataBaseHelper = null;
        OpenHelperManager.releaseHelper();
        super.onDestroy();

    }
}
