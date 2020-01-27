package com.ecs.demotestuapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.model.FetchProductInput;
import com.ecs.demotestuapp.model.PropertyItem;

public class FetchProductsInputActivity extends AppCompatActivity {

    private PropertyItem propertyItem;
    private FetchProductInput fetchProductInput;

    EditText et_pageNumber,et_pageSize ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_product);

        et_pageNumber = findViewById(R.id.et_pageNumber);
        et_pageSize = findViewById(R.id.et_pageSize);

        Bundle bundle = getIntent().getExtras();
        propertyItem = (PropertyItem) bundle.getSerializable("property");

        fetchProductInput = propertyItem.fetchProductInput;
    }


    public void execute(View view) {
        int  pageSize = Integer.valueOf(et_pageSize.getText().toString().trim());
        int  pageNumber = Integer.valueOf(et_pageNumber.getText().toString().trim());
        fetchProductInput.setPageNumber(pageNumber);
        fetchProductInput.setPageSize(pageSize);

        Intent intent = new Intent(this, EcsDemoResultActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("property",propertyItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
