package com.ecs.demotestuapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.model.FetchProductInput;
import com.ecs.demotestuapp.model.PropertyItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;

import java.util.ArrayList;
import java.util.List;

public class SetDeliveryModeInputActivity extends AppCompatActivity {

    private PropertyItem propertyItem;

    Spinner spinner;

    ECSDeliveryMode ecsDeliveryMode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_deliverymode);

        spinner = findViewById(R.id.spinner);

        Bundle bundle = getIntent().getExtras();
        propertyItem = (PropertyItem) bundle.getSerializable("property");

        List<ECSDeliveryMode> ecsDeliveryModes = ECSDataHolder.INSTANCE.getEcsDeliveryModes();

        if(ecsDeliveryModes!=null && !ecsDeliveryModes.isEmpty()) {
            List<String> deliveryModeIDs = new ArrayList<>();

            for (ECSDeliveryMode ecsDeliveryMode : ecsDeliveryModes) {
                deliveryModeIDs.add(ecsDeliveryMode.getCode());
            }


            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deliveryModeIDs);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner.setAdapter(aa);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String deliveryMode = deliveryModeIDs.get(position);

                    ecsDeliveryMode = getDeliveryModeFromID(deliveryMode);
                    ECSDataHolder.INSTANCE.setEcsDeliveryMode(ecsDeliveryMode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ecsDeliveryMode = ecsDeliveryModes.get(0);
                    ECSDataHolder.INSTANCE.setEcsDeliveryMode(ecsDeliveryMode);
                }
            });
        }

    }

    private ECSDeliveryMode getDeliveryModeFromID(String deliveryMode) {

        List<ECSDeliveryMode> ecsDeliveryModes = ECSDataHolder.INSTANCE.getEcsDeliveryModes();

        for(ECSDeliveryMode ecsDeliveryMode:ecsDeliveryModes){
            if(ecsDeliveryMode.getCode().equalsIgnoreCase(deliveryMode)){
                return ecsDeliveryMode;
            }
        }


        return ecsDeliveryModes.get(0);
    }


    public void execute(View view) {

        if(ecsDeliveryMode ==null){
            Toast.makeText(this,"Delivery mode is not set",Toast.LENGTH_SHORT).show();

        }else {

            Intent intent = new Intent(this, EcsDemoResultActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("property", propertyItem);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
