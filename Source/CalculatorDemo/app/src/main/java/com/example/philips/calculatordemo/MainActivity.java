package com.example.philips.calculatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.DoubleBuffer;

import static android.R.attr.onClick;
import static com.example.philips.calculatordemo.R.styleable.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button addBtn, subBtn, mulBtn, divBtn;
    EditText firstNum, secondNum;
    TextView result;
    Double first, second, answer;
    CalculatorBrain calculatorBrain;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
        subBtn = (Button) findViewById(R.id.subBtn);
        subBtn.setOnClickListener(this);
        mulBtn = (Button) findViewById(R.id.mulBtn);
        mulBtn.setOnClickListener(this);
        divBtn = (Button) findViewById(R.id.divBtn);
        divBtn.setOnClickListener(this);
        firstNum = (EditText) findViewById(R.id.editTextFirst);
        secondNum = (EditText) findViewById(R.id.editTextSecond);
        result = (TextView) findViewById(R.id.result);
        calculatorBrain = new CalculatorBrain();
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(firstNum.getText().toString()) && !TextUtils.isEmpty(secondNum.getText().toString())) {
            first = Double.parseDouble(firstNum.getText().toString());
            second = Double.parseDouble(secondNum.getText().toString());
            switch (v.getId()) {
                case R.id.addBtn:
                    answer = calculatorBrain.add(first,second);break;
                case R.id.subBtn:
                    answer = calculatorBrain.sub(first,second);break;
                case R.id.mulBtn:
                    answer = calculatorBrain.mul(first,second);break;
                case R.id.divBtn:
                    answer = calculatorBrain.div(first,second);break;
            }
            result.setText("Result = "+ answer.toString());
        }
    }
}

