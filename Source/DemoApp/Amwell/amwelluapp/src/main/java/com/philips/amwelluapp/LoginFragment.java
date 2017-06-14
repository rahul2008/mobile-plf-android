package com.philips.amwelluapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.ConsumerInfo;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * Created by philips on 6/1/17.
 */

public class LoginFragment extends Fragment implements BackEventListener {
    Button loginButton;
    EditText email;
    EditText password;

    @Override
    public boolean handleBackEvent() {
        return false;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guest_login, container, false);
        email = (EditText) view.findViewById(R.id.login_email);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.email_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                GlobalValues.mAWSDK.authenticate(userName, userPassword, null, callback);

            }
        });


        return view;
    }

    SDKCallback<Authentication, SDKError> callback = new SDKCallback<Authentication, SDKError>() {
        @Override
        public void onResponse(Authentication authentication, SDKError sdkError) {

            if(null!=authentication) {
                Log.v("AWSDK","Authentication Successful");
              ConsumerInfo  consumerInfo = authentication.getConsumerInfo();
                Log.v("AWSDK",authentication.toString());
                Toast.makeText(getActivity(),"Login Success", Toast.LENGTH_SHORT).show();

            }else{
                Log.v("AWSDK","Authentication Failed");
                Toast.makeText(getActivity(),"Login Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable throwable) {
            Log.v("AWSDK","Authentication Failed");
            Toast.makeText(getActivity(),"Login Failed", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
