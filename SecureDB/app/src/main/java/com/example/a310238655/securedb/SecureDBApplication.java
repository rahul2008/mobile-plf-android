/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.example.a310238655.securedb;

import android.app.Application;
import android.util.Log;


import com.example.a310238655.securedb.SQLCipherORMLitePOC.Contact;
import com.example.a310238655.securedb.SQLCipherORMLitePOC.FeedReaderDbHelper;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SecureDBApplication extends Application {
    FeedReaderDbHelper mFeedReaderDbHelper;
    public static SimpleDateFormat df;

    @Override
    public void onCreate() {
        super.onCreate();

        df = new SimpleDateFormat("HH:mm:ss.SSS a", Locale.ENGLISH);

        mFeedReaderDbHelper = new FeedReaderDbHelper(getApplicationContext());

        /**
         * CRUD Operations
         * */
        // Inserting Contacts

        String InsertStartTime = getLocalTimestamp();
        Log.d("Insert Start Time: ", "Inserting .." + getLocalTimestamp());
        Contact mContact = new Contact("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", "1234500000", "");
        for (int i = 0; i < 1000; i++) {
            mContact.setNumber("1234500000" + i);
            if (i % 100 == 0) {
                mContact.setGroupMaxNumberNumber("" + i);
            } else {
                mContact.setGroupMaxNumberNumber("");
            }

            mFeedReaderDbHelper.addContact(mContact);

        }
        Log.d("Insert End Time: ", "Insert End .." + getLocalTimestamp());
        String InsertEndTime = getLocalTimestamp();
        getFinalTime(InsertStartTime, InsertEndTime, "InsertTime");


        Log.d("Update Start Time: ", "Update.." + getLocalTimestamp());
        String UpdateStart = getLocalTimestamp();
        for (int i = 0; i < 1000; i++) {
            mContact.setNumber("" + i);
            mContact.setID(i);
            mContact.setName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz" + i);
            if (i % 10 == 0) {
                mContact.setGroupMaxNumberNumber("" + i + 1);
            } else {
                mContact.setGroupMaxNumberNumber("");
            }

            mFeedReaderDbHelper.updateContact(mContact);

        }
        Log.d("Update End Time: ", "Update.." + getLocalTimestamp());
        String UpdateEnd = getLocalTimestamp();
        getFinalTime(UpdateStart, UpdateEnd, "UpdateTime");


//        Log.d("Read Start Time: ", "Read.." + getLocalTimestamp());
//        String ReadStart = getLocalTimestamp();
//        mFeedReaderDbHelper.getAllContacts();
//        Log.d("Read End Time: ", "Read.." + getLocalTimestamp());
//        String ReadEnd = getLocalTimestamp();
//        getFinalTime(ReadStart, ReadEnd, "Read Time");

    }

    public static String getLocalTimestamp() {

        String mLocalTimestamp;
        Calendar c = Calendar.getInstance();
        mLocalTimestamp = df.format(c.getTime());
        return mLocalTimestamp;
    }

    public static void getFinalTime(String start, String end, String TAG) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = df.parse(start);
            endDate = df.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffInMs = endDate.getTime() - startDate.getTime();

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
//        long difference = endDate.getTime() - startDate.getTime();
//        if (difference < 0) {
//            Date dateMax = null;
//            Date dateMin = null;
//            try {
//                dateMax = df.parse("24:00");
//                dateMin = df.parse("00:00");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
//        }
//        int days = (int) (difference / (1000 * 60 * 60 * 24));
//        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.i(TAG, "  diffInMs: " + diffInMs);
    }

}
