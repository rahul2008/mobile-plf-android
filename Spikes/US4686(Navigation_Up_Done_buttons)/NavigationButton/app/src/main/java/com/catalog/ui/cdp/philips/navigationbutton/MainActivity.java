package com.catalog.ui.cdp.philips.navigationbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CustomViewInflation customViewInflation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customViewInflation = (CustomViewInflation)findViewById(R.id.custom);

        customViewInflation.getTextView1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(MainActivity.this," Clicked View1",Toast.LENGTH_SHORT).show();
            }
        });

        customViewInflation.getTextView2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(MainActivity.this," Clicked View2",Toast.LENGTH_SHORT).show();
            }
        });

        customViewInflation.getTextView3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(MainActivity.this," Clicked View3",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
