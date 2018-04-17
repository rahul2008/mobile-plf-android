package com.philips.platform.neu.demouapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.neura.resources.authentication.AnonymousAuthenticationStateListener;
import com.neura.resources.authentication.AuthenticationState;
import com.neura.resources.user.UserDetails;
import com.neura.resources.user.UserDetailsCallbacks;
import com.neura.sdk.object.SubscriptionMethod;
import com.neura.sdk.service.SubscriptionRequestCallbacks;
import com.neura.sdk.util.NeuraUtil;
import com.philips.platform.neu.demouapp.neura.NeuraManager;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NueraDemoMainActivity extends UIDActivity{
    private String userId;
    CheckBoxAdapter adapter;
    Button connect,disconnect;
    RadioGroup radioGroup;
    int counter;
    List<String> momentsList = Arrays.asList(
            "userStartedWalking",
            "userIsIdleAtHome",
            "userFinishedWalking",
            "userStartedRunning",
            "userFinishedRunning",
            "userIsIdleFor1Hour");
    boolean [] array = new boolean[momentsList.size()];
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
        counter = 0;
        connect = findViewById(R.id.ConnectButton);
        disconnect = findViewById(R.id.DisconnectButton);
        radioGroup = findViewById(R.id.RadioGroup);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeuraManager.authenticateAnonymously(silentStateListener);
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeuraManager.getInstance().getClient().forgetMe(getParent(), true, new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        setDisconnected();
                        return false;
                    }
                });
            }
        });
    }

    private void setDisconnected() {
        disconnect.setVisibility(View.GONE);
        connect.setVisibility(View.VISIBLE);
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
                    setConnected();
                    NeuraManager.getInstance().getClient().unregisterAuthStateListener();
                    Log.d("Neura","AuthenticatedAnonymously callback received");


                    // do something with the user's details...
                    getUserDetails();

                    // Trigger UI changes
                    boolean isConnected = true;
                    boolean setSymbol = true;
//                    setUIState(isConnected, setSymbol);

                    // Subscribe to neura moments so that you can receive push notifications
                    subscribeToPushEvents();
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

    private void setConnected() {
        disconnect.setVisibility(View.VISIBLE);
        connect.setVisibility(View.GONE);
    }

    private void getUserDetails() {
        NeuraManager.getInstance().getClient().getUserDetails(new UserDetailsCallbacks() {
            @Override
            public void onSuccess(UserDetails userDetails) {
                if (userDetails.getData() != null) {
                    // Do something with this information
                    userId = userDetails.getData().getNeuraId();
                    NeuraManager.getInstance().getClient().getUserAccessToken();
                }
            }

            @Override
            public void onFailure(Bundle resultData, int errorCode) {
            }
        });
    }

    private void displayList() {
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
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
                        if(cb.isSelected()){
                            array[position] = true;
                        }else{
                            array[position] = false;
                        }
                    }
                });

                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            counter++;
                        }else {
                            counter --;
                        }
                        if(counter > 0){
                            connect.setEnabled(true);
                        }else{
                            connect.setEnabled(false);
                        }
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

    private void subscribeToPushEvents() {
        /**
         * Go to our push notification guide for more info on how to register receiving
         * events via firebase https://dev.theneura.com/docs/guide/android/pushnotification.
         * If you're receiving a 'Token already exists error',make sure you've initiated a
         * Firebase instance like {@link com.neura.sampleapplication.activities.MainActivity#onCreate(Bundle)}
         * http://stackoverflow.com/a/38945375/5130239
         */
        NeuraManager.getInstance().getClient().
                registerFirebaseToken(FirebaseInstanceId.getInstance().getToken());

        List<String> events = getSelectedEvents();
        //Subscribing to events - mandatory in order to receive events.
        for (int i = 0; i < events.size(); i++) {
            subscribeToEvent(events.get(i));
        }
    }

    private List<String> getSelectedEvents() {
        List<String> selectedEvents = new ArrayList<>();
        for (int i = 0; i < momentsList.size(); i++) {
            if(array[i]){
                selectedEvents.add(momentsList.get(i));
            }
        }
        return selectedEvents;
    }


    public void subscribeToEvent(final String eventName) {
        final String eventIdentifier = userId + eventName;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.FCM){
                    NeuraManager.getInstance().getClient().subscribeToEvent(eventName, eventIdentifier, mSubscribeRequest);
                }else if(checkedId == R.id.WebHook){
                    NeuraManager.getInstance().getClient().subscribeToEvent(eventName,eventIdentifier, SubscriptionMethod.WEBHOOK,"WebHookIdHere",mSubscribeRequest);
                }
            }
        });

    }

    private SubscriptionRequestCallbacks mSubscribeRequest = new SubscriptionRequestCallbacks() {
        @Override
        public void onSuccess(final String eventName, Bundle resultData, String identifier) {
            //loadProgress(false);
            Toast.makeText(getApplicationContext(),"Successfully subscribed to event: " + eventName,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(String eventName, Bundle resultData, int errorCode) {
            Toast.makeText(getApplicationContext(),
                    "Error: Failed to subscribe to event " + eventName + ". Error code: " +
                            NeuraUtil.errorCodeToString(errorCode), Toast.LENGTH_SHORT).show();
        }
    };
}
