package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;

public class SpringBoardsctivity extends CatalogActivity {
    Intent i1,i2,i3 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_boardsctivity);
        Button block=(Button)findViewById(R.id.full_block);
         i1= new Intent(this, SpringboardFullBlocksActivity.class);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);

            }
        });

        Button block2=(Button)findViewById(R.id.grid6_block);
        i2= new Intent(this, SpringBoardSixGridActivity.class);
        block2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);

            }
        });

        Button block3=(Button)findViewById(R.id.list);
        i3= new Intent(this, SpringBoardListActivity.class);
        block3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);

            }
        });
    }

}
