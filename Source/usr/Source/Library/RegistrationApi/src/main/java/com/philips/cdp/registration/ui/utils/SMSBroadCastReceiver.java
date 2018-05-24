package com.philips.cdp.registration.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.philips.cdp.registration.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by philips on 5/21/18.
 */

public class SMSBroadCastReceiver extends BroadcastReceiver {

    public interface ReceiveAndRegisterOTPListener {

        void registerSMSReceiver();

        void unRegisterSMSReceiver();

        void onOTPReceived(String otp);

        Activity getActivityContext();

        SMSBroadCastReceiver getSMSBroadCastReceiver();
    }

    private final ReceiveAndRegisterOTPListener mReceiveAndRegisterOTPListener;
    public static final String OTP_REGEX = "[0-9]{1,6}";
    public static final int SMS_PERMISSION_CODE = 1000;
    private final String TAG =  this.getClass().getSimpleName();

    public SMSBroadCastReceiver(ReceiveAndRegisterOTPListener mReceiveAndRegisterOTPListener) {

        this.mReceiveAndRegisterOTPListener = mReceiveAndRegisterOTPListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RLog.i(TAG,"SMS onRecieve is Called");
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            RLog.i(TAG,"Sender Number"+sender);
            //Check the sender to filter messages which we require to read
            if (sender.equals(context.getString(R.string.otp_sender))) {

                String messageBody = smsMessage.getMessageBody();

                getOTPFromMessage(messageBody);
            }
        }

    }

    private void getOTPFromMessage(String messageBody) {

        Pattern pattern = Pattern.compile(OTP_REGEX);
        Matcher matcher = pattern.matcher(messageBody);

        String otp = "";

        while (matcher.find()) {
            otp = matcher.group();
            mReceiveAndRegisterOTPListener.onOTPReceived(otp);
        }
    }

    /**
     * Check if we have SMS permission
     */
    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(mReceiveAndRegisterOTPListener.getActivityContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    public void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mReceiveAndRegisterOTPListener.getActivityContext(), Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(mReceiveAndRegisterOTPListener.getActivityContext(), new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(999);
        mReceiveAndRegisterOTPListener.getActivityContext().registerReceiver(mReceiveAndRegisterOTPListener.getSMSBroadCastReceiver(), intentFilter);
    }

    public void unRegisterReceiver() {
        mReceiveAndRegisterOTPListener.getActivityContext().unregisterReceiver(mReceiveAndRegisterOTPListener.getSMSBroadCastReceiver());
    }

}

