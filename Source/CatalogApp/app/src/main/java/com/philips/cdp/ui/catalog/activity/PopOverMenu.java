package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PopOverMenu extends CatalogActivity {

    private UIKitListPopupWindow listpopupwindowTopLeft;
    private UIKitListPopupWindow listpopupwindowTopRight;
    private UIKitListPopupWindow listpopupwindowLeft;
    private UIKitListPopupWindow listpopupwindowRight;
    private UIKitListPopupWindow listpopupwindowBottomLeft;
    private UIKitListPopupWindow listpopupwindowBottomRight;


    private Button topleft;
    private Button topright;
    private Button left;
    private Button right;
    private Button buttomleft;
    private Button buttomright;


    public  final String[] descriptions = new String[] {
            "Setting",
            "Share", "Mail",
            "Chat" };

    public   Integer[] images = {R.drawable.uikit_apple, R.drawable.uikit_share,
            R.drawable.uikit_envelope,R.drawable.uikit_ballon, };

    List<RowItem> rowItems1;
    List<RowItem> rowItems2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        rowItems1 = new ArrayList<RowItem>();
        rowItems2 = new ArrayList<RowItem>();

        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share) , descriptions[1]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));



        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share) , descriptions[1]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share) , descriptions[1]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));

        //rowItems3.;

        setContentView(R.layout.activity_pop_over_menu2);
        init();

        listpopupwindowTopLeft  = new UIKitListPopupWindow(PopOverMenu.this, topleft, UIKitListPopupWindow.Type.TOPLEFT, rowItems1);
        listpopupwindowTopRight = new UIKitListPopupWindow(PopOverMenu.this,topright,UIKitListPopupWindow.Type.TOPRIGHT, rowItems1);
        listpopupwindowLeft     = new UIKitListPopupWindow(PopOverMenu.this,left,UIKitListPopupWindow.Type.LEFT, rowItems1);
        listpopupwindowRight    = new UIKitListPopupWindow(PopOverMenu.this,right,UIKitListPopupWindow.Type.RIGHT, rowItems2);
        listpopupwindowBottomLeft  = new UIKitListPopupWindow(PopOverMenu.this,buttomleft,UIKitListPopupWindow.Type.BOTTOMLEFT, rowItems1);
        listpopupwindowBottomRight = new UIKitListPopupWindow(PopOverMenu.this,buttomright,UIKitListPopupWindow.Type.BOTTOMRIGHT, rowItems1);



        topleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowTopLeft.show();
            }
        });



        topright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowTopRight.show();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowLeft.show();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowRight.show();
            }
        });


        buttomleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowBottomLeft.show();
            }
        });


        buttomright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listpopupwindowBottomRight.show();
            }
        });
    }


    void init() {
        topleft = (Button)findViewById(R.id.topleft);
        topright = (Button) findViewById(R.id.topright);
        left = (Button) findViewById(R.id.left);
        right = (Button)findViewById(R.id.right);
        buttomleft = (Button) findViewById(R.id.bottomleft);
        buttomright = (Button) findViewById(R.id.bottomright);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_pop_over_menu, menu);

       /* //final View menuItemView = findViewById(R.id.menu_item);

        //menu.add(listpopupwindowTopLeft);

        try {
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
               // menuKeyField.setBoolean(config, false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //listpopupwindowTopLeft.show();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {


            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if( menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                   // Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        menu.getItem(0).setIcon(VectorDrawable.create(this,R.drawable.uikit_gear_19_19));
        menu.getItem(1).setIcon(VectorDrawable.create(this,R.drawable.uikit_share));
        menu.getItem(2).setIcon(VectorDrawable.create(this,R.drawable.uikit_envelope));
        menu.getItem(3).setIcon(VectorDrawable.create(this,R.drawable.uikit_ballon));


        return super.onPrepareOptionsMenu(menu);


    }



}
