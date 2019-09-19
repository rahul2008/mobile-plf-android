package com.ecs.demotestuapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.model.FetchProductInput;
import com.ecs.demotestuapp.model.PropertyItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.model.products.ECSProduct;

import java.util.ArrayList;
import java.util.List;

public class FetchSummariesInputActivity extends AppCompatActivity {

    private PropertyItem propertyItem;

    EditText et_ctns;

    TextView textView;

    List<String> ctns;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_summaries);

        ctns = new ArrayList<>();

        et_ctns = findViewById(R.id.et_ctns);

        textView =  findViewById(R.id.tv_ctn_sample);

        Bundle bundle = getIntent().getExtras();
        propertyItem = (PropertyItem) bundle.getSerializable("property");


        if(ECSDataHolder.INSTANCE.getEcsProducts()!=null){

            List<ECSProduct> products = ECSDataHolder.INSTANCE.getEcsProducts().getProducts();

            if(products!=null){
                getStringFromList(products);
            }
        }
    }

    private void getStringFromList(List<ECSProduct> products) {

        StringBuilder sb = new StringBuilder("Sample CTNS : \n");

        for(ECSProduct ecsProduct:products){
            sb.append(ecsProduct.getCode()+"\n");
        }

        textView.setText(sb);
    }


    public void execute(View view) {

        propertyItem.setCtns(ctns);
        Intent intent = new Intent(this, EcsDemoResultActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("property",propertyItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void addCtn(View view) {

        String  addedCtns = et_ctns.getText().toString().trim();
        ctns.add(addedCtns);
        et_ctns.setText("");
    }
}
