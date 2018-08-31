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

import com.philips.cdp.registration.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.requestPermissions;

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

    static final IntentFilter intentFilter;

    static {
        intentFilter = new IntentFilter();
        intentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(999);
    }

    private final ReceiveAndRegisterOTPListener mReceiveAndRegisterOTPListener;
    public static final String OTP_REGEX = "[0-9]{1,6}";
    public static final int SMS_PERMISSION_CODE = 1000;
    private final String TAG = this.getClass().getSimpleName();

    public SMSBroadCastReceiver(ReceiveAndRegisterOTPListener mReceiveAndRegisterOTPListener) {

        this.mReceiveAndRegisterOTPListener = mReceiveAndRegisterOTPListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RLog.i(TAG, "onRecieve : SMS received");
        getDataFromIntent(context, intent);

    }

    private void getDataFromIntent(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (Object object : pdus) {

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
            String sender = smsMessage.getDisplayOriginatingAddress();
            RLog.d(TAG, "Sender Number" + sender + " getOriginatingAddress " + smsMessage.getOriginatingAddress());
            RLog.d(TAG, "Sender Number" + sender + " getServiceCenterAddress " + smsMessage.getServiceCenterAddress());

            RLog.d(TAG, "Sender Number" + sender + "Validation number starts with" + context.getString(R.string.otp_sender));
            //Check the sender to filter messages which we require to read

            if (sender.startsWith(context.getString(R.string.otp_sender))) {

                String messageBody = smsMessage.getMessageBody();
                RLog.d(TAG, "Sender Message" + messageBody);
                getOTPFromMessage(messageBody);
            }
        }
    }

    void getOTPFromMessage(String messageBody) {

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

        requestPermissions(mReceiveAndRegisterOTPListener.getActivityContext(), new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    public void registerReceiver() {
        RLog.d(TAG, "registerReceiver");
        mReceiveAndRegisterOTPListener.getActivityContext().registerReceiver(mReceiveAndRegisterOTPListener.getSMSBroadCastReceiver(), intentFilter);
    }

    public void unRegisterReceiver() {
        try {
            mReceiveAndRegisterOTPListener.getActivityContext().unregisterReceiver(mReceiveAndRegisterOTPListener.getSMSBroadCastReceiver());
        } catch (Exception e) {
            RLog.e(TAG, "unRegisterReceiver: ReceiveAndRegisterOTPListener is not set"+e.getMessage());
        }
    }

}

