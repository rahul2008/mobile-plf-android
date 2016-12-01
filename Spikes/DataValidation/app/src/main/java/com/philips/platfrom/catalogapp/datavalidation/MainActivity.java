package com.philips.platfrom.catalogapp.datavalidation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.platfrom.catalogapp.datavalidation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ValidationTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        textView = binding.sampleErrorView;
        binding.setActivity(this);
    }

    public void showError() {
        textView.showValidationError();
    }

    public void dismissError() {
        textView.dismissError();
    }
}
