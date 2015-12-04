package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;

public class SpringBoardsctivity extends CatalogActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_boardsctivity);
        Button block=(Button)findViewById(R.id.full_block);
         i= new Intent(this, SpringboardFullBlocksActivity.class);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);

            }
        });
    }

}
