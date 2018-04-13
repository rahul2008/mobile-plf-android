package com.philips.platform.neu.demouapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.neura.resources.authentication.AnonymousAuthenticationStateListener;
import com.neura.resources.authentication.AuthenticationState;
import com.philips.platform.neu.demouapp.neura.NeuraManager;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NueraDemoMainActivity extends UIDActivity{

    CheckBoxAdapter adapter;
    Button connect,disconnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuera_activity_main);
        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException ex) {
            FirebaseApp.initializeApp(this, FirebaseOptions.fromResource(this));
        }
        NeuraManager.getInstance().initNeuraConnection(getApplicationContext());
        requestLocationPermission();
        displayList();
        connect = findViewById(R.id.ConnectButton);
        disconnect = findViewById(R.id.DisconnectButton);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeuraManager.authenticateAnonymously(silentStateListener);
            }
        });
    }



    AnonymousAuthenticationStateListener silentStateListener = new AnonymousAuthenticationStateListener() {
        @Override
        public void onStateChanged(AuthenticationState state) {
            switch (state) {
                case AccessTokenRequested:
                    Log.d("Neura","AccessTokenRequested callback received");
                    break;
                case AuthenticatedAnonymously:
                    // successful authentication
                    NeuraManager.getInstance().getClient().unregisterAuthStateListener();
                    Log.d("Neura","AuthenticatedAnonymously callback received");
                    // do something with the user's details...
//                    getUserDetails();

                    // Trigger UI changes
                    boolean isConnected = true;
                    boolean setSymbol = true;
//                    setUIState(isConnected, setSymbol);

                    // Subscribe to neura moments so that you can receive push notifications
//                    subscribeToPushEvents();
                    break;
                case NotAuthenticated:
                case FailedReceivingAccessToken:
                    // Authentication failed indefinitely. a good opportunity to retry the authentication flow
                    Log.d("Neura","Failed  callback received");
                    NeuraManager.getInstance().getClient().unregisterAuthStateListener();

                    // Trigger UI changes
                    boolean enabled = true;
//                    loadProgress(!enabled);
//                    mRequestPermissions.setEnabled(enabled);
                    break;
                default:
            }
        }
    };
    private void displayList() {
       List<String> momentsList = Arrays.asList(
               "Presence at Home",
               "Physical Activity",
               "Sleep-related Habits",
               "Presence at a Certain Type of Places",
               "Driving Activity and Habits",
               "Presence at Work");

       adapter = new CheckBoxAdapter(this,R.layout.checkbox_info,momentsList);
        ListView listView = findViewById(R.id.ListView);
        listView.setAdapter(adapter);

    }

    private class CheckBoxAdapter extends ArrayAdapter<String>{
        private List<String> momentsList;
        public CheckBoxAdapter(@NonNull Context context, int resource,List<String> momentsList) {
            super(context, resource, momentsList);
            this.momentsList = new ArrayList<>();
            this.momentsList.addAll(momentsList);

        }

        private class ViewHolder{
            android.widget.CheckBox checkBox;
            TextView MomentList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.checkbox_info,parent,false);

                viewHolder = new ViewHolder();
                viewHolder.MomentList = convertView.findViewById(R.id.code);
                viewHolder.checkBox = convertView.findViewById(R.id.checkBox1);
                convertView.setTag(viewHolder);

                viewHolder.checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        android.widget.CheckBox cb = (android.widget.CheckBox) v ;
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String moment = momentsList.get(position);
            viewHolder.MomentList.setText(moment);
            viewHolder.checkBox.setSelected(true);
            return convertView;
        }
    }

    private void requestLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION}, 1111);
            return;
        }
        else{
            // Make sure the user enables location,
            // this is needed to for Neura to work, and is not automatic when using anonymous authentication.
            // Phone based auth asks for it automatically.
            Log.d("Neura","Neura Works only for location enabled");
        }
    }
}
