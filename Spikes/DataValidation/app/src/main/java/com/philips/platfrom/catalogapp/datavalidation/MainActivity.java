package com.philips.platfrom.catalogapp.datavalidation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ValidationTextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (ValidationTextView) findViewById(R.id.sample_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                textView.showValidationError();
            }
        });
    }
}
