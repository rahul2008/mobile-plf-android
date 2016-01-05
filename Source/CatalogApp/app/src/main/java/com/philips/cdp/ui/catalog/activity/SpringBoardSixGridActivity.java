/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class SpringBoardSixGridActivity extends CatalogActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String[] prgmNameList = {"Shop ", " Stats ", " Factory ", "Monitor", "Wellness", "Settings"};
        Drawable[] prgmImages = {(VectorDrawable.create(this, R.drawable.uikit_cart_large)), (VectorDrawable.create(this, R.drawable.uikit_stats_39x32)), (VectorDrawable.create(this, R.drawable.uikit_factory_large)), (VectorDrawable.create(this, R.drawable.uikit_monitor)), (VectorDrawable.create(this, R.drawable.uikit_apple_large)), (VectorDrawable.create(this, R.drawable.uikit_gear_large))};

        setNoActionBarTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.uikit_springboard_sixblocks);
        LinearLayout ll = (LinearLayout) findViewById(R.id.row_layout1);
        //   ll.setVisibility(View.GONE);
        ImageView imv1 = (ImageView) findViewById(R.id.imageView1);
        imv1.setImageDrawable(prgmImages[0]);

        TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText(prgmNameList[0]);

        ImageView imv2 = (ImageView) findViewById(R.id.imageView2);
        imv2.setImageDrawable(prgmImages[1]);

        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText(prgmNameList[1]);

        ImageView imv3 = (ImageView) findViewById(R.id.imageView3);
        imv3.setImageDrawable(prgmImages[2]);
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        tv3.setText(prgmNameList[2]);
        ImageView imv4 = (ImageView) findViewById(R.id.imageView4);
        imv4.setImageDrawable(prgmImages[3]);
        TextView tv4 = (TextView) findViewById(R.id.textView4);
        tv4.setText(prgmNameList[3]);
        ImageView imv5 = (ImageView) findViewById(R.id.imageView5);
        imv5.setImageDrawable(prgmImages[4]);
        TextView tv5 = (TextView) findViewById(R.id.textView5);
        tv5.setText(prgmNameList[4]);
        ImageView imv6 = (ImageView) findViewById(R.id.imageView6);
        imv6.setImageDrawable(prgmImages[5]);
        TextView tv6 = (TextView) findViewById(R.id.textView6);
        tv6.setText(prgmNameList[5]);


    }


}

