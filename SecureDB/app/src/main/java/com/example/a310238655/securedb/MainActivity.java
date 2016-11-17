package com.example.a310238655.securedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.a310238655.securedb.SQLCipherORMLitePOC.FeedReaderDbHelper;

import static com.example.a310238655.securedb.SecureDBApplication.getFinalTime;
import static com.example.a310238655.securedb.SecureDBApplication.getLocalTimestamp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FeedReaderDbHelper mFeedReaderDbHelper = new FeedReaderDbHelper(getApplicationContext());
        Log.d("Read Start Time: ", "Read.." + getLocalTimestamp());
        String ReadStart = getLocalTimestamp();
        mFeedReaderDbHelper.getAllContacts();
        Log.d("Read End Time: ", "Read.." + getLocalTimestamp());
        String ReadEnd = getLocalTimestamp();
        getFinalTime(ReadStart, ReadEnd, "Read Time");
    }
}
