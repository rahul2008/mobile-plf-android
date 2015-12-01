package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.SpringBoardCustomAdapter;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;

/**
 * Created by 310213373 on 11/30/2015.
 */
public class SpringboardFullBlocksActivity extends  CatalogActivity {

    GridView gv;
    Context context;
    ArrayList prgmName;
    SpringBoardCustomAdapter spd;
  //  public static String [] prgmNameList={"Telephone "," Moniter ","Alarm Clock"," Factory ","Stats","Message","Shopping","Settings"};
  //  public static int [] prgmImages={R.drawable.call,R.drawable.apple,R.drawable.alarm,R.drawable.barchart,R.drawable.mail, VectorDrawable.create(this, R.drawable.uikit_cart),R.drawable.cross_icon,R.drawable.gear};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         String [] prgmNameList={"Telephone "," Moniter ","Alarm Clock"," Factory ","Stats","Message","Shopping","Settings"};
        Drawable [] prgmImages={(VectorDrawable.create(this, R.drawable.uikit_cart)),(VectorDrawable.create(this, R.drawable.uikit_cart)),(VectorDrawable.create(this, R.drawable.uikit_cart)),(VectorDrawable.create(this, R.drawable.uikit_stats)),(VectorDrawable.create(this, R.drawable.uikit_cart)), (VectorDrawable.create(this, R.drawable.uikit_cart)),(VectorDrawable.create(this, R.drawable.uikit_apple)),(VectorDrawable.create(this, R.drawable.uikit_gear))};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_springboard);
        gv=(GridView) findViewById(R.id.gridView1);
        spd=new SpringBoardCustomAdapter(this, prgmNameList, prgmImages);
        gv.setAdapter(spd);
    }


}
