package com.example.a310238655.securedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a310238655.securedb.SQLCipherORMLitePOC.Contact;
import com.example.a310238655.securedb.SQLCipherORMLitePOC.FeedReaderDbHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FeedReaderDbHelper mFeedReaderDbHelper;
    private static SimpleDateFormat df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button insertButton = (Button) findViewById(R.id.button);
        Button updateButton = (Button) findViewById(R.id.button2);
        Button readButton = (Button) findViewById(R.id.button3);
        SQLiteDatabase.loadLibs(this);

        df = new SimpleDateFormat("HH:mm:ss.SSS a", Locale.ENGLISH);

        mFeedReaderDbHelper = new FeedReaderDbHelper(getApplicationContext());

        /**
         * CRUD Operations
         * */
        // Inserting Contacts

        final Contact mContact = new Contact("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", "1234500000", "");


        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String InsertStartTime = getLocalTimestamp();
                Log.d("Insert Start Time: ", "Inserting .." + getLocalTimestamp());


                mFeedReaderDbHelper.addContact(mContact);

                Log.d("Insert End Time: ", "Insert End .." + getLocalTimestamp());

                String InsertEndTime = getLocalTimestamp();
                getFinalTime(InsertStartTime, InsertEndTime, "InsertTime");

            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Update Start Time: ", "Update.." + getLocalTimestamp());
                String UpdateStart = getLocalTimestamp();
                mFeedReaderDbHelper.updateContact(mContact);
                Log.d("Update End Time: ", "Update.." + getLocalTimestamp());
                String UpdateEnd = getLocalTimestamp();
                getFinalTime(UpdateStart, UpdateEnd, "UpdateTime");

            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Read Start Time: ", "Read.." + getLocalTimestamp());
                String ReadStart = getLocalTimestamp();
                mFeedReaderDbHelper.getAllContacts();
                Log.d("Read End Time: ", "Read.." + getLocalTimestamp());

                String ReadEnd = getLocalTimestamp();
                getFinalTime(ReadStart, ReadEnd, "Read Time");
            }
        });


    }


    private String getLocalTimestamp() {

        String mLocalTimestamp;
        Calendar c = Calendar.getInstance();
        mLocalTimestamp = df.format(c.getTime());
        return mLocalTimestamp;
    }

    private void getFinalTime(String start, String end, String TAG) {
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

        Log.i(TAG, "  diffInMs: " + diffInMs);
    }

}
