
package com.ecs.demotestuapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ecs.demotestuapp.R;



public class ResultActivity extends AppCompatActivity {

    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        tvResult = findViewById(R.id.tv_result);

        String result = getIntent().getStringExtra("result");
        tvResult.setText("Result\n\n"+result);
    }

  }