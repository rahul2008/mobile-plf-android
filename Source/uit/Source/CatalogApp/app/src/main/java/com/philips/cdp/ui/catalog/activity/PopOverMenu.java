package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.RowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Attributes defined for Action Bar overflow menu customization:</b>

 <br>
 <b>1)attr for menu item image : Add below line in your theme to override UIKIt style</b>

 <pre><&ltitem name="popoverImageStyle">@style/YourStyle</item></pre>

 Defult is the below style.
 <br>
 Example:

 <style name="PopOverImageStyle" parent="android:Widget.ListView.DropDown">
 <item name="android:tint">?attr/uikit_darkerColor</item>
 </style>


 2) attrs for divider and colorbackground for menu list

 <item name="dropDownListViewStyle">@style/PDropDownStyle</item>

 <br>
 Example:

 <style name="PDropDownStyle" parent="android:Widget.ListView.DropDown">
 <item name="android:divider">@drawable/line</item>
 <item name="android:colorBackground">?attr/uikit_veryLightColor</item>
 </style>



 3) attrs for menulist selector

 <item name="veryLightselectoropacityfifteen">@drawable/your_selector</item>
 <br>
 Example: Default is below selector

 <selector xmlns:android="http://schemas.android.com/apk/res/android">
 <item android:drawable="@color/uikit_philips_very_light_blue_with_opacity_15" android:state_pressed="true"/>
 <item android:drawable="@color/uikit_white" android:state_pressed="false"/>
 </selector>
 */

public class PopOverMenu extends CatalogActivity {

    public final String[] descriptions = new String[]{
            "Setting",
            "Share", "Mail",
            "Chat"};
    public Integer[] images = {R.drawable.uikit_apple, R.drawable.uikit_share_19_18,
            R.drawable.uikit_envelope, R.drawable.uikit_ballon,};
    List<RowItem> rowItems1;
    List<RowItem> rowItems2;
    List<RowItem> rowItems3;
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
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        rowItems1 = new ArrayList<RowItem>();
        rowItems2 = new ArrayList<RowItem>();
        rowItems3 = new ArrayList<RowItem>();

        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share_19_18) , descriptions[1]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems1.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));



        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share_19_18) , descriptions[1]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_gear_19_19) , descriptions[0]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_share_19_18) , descriptions[1]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_envelope) , descriptions[2]));
        rowItems2.add(new RowItem(VectorDrawable.create(this, R.drawable.uikit_ballon) , descriptions[3]));


        rowItems3.add(new RowItem(descriptions[0]));
        rowItems3.add(new RowItem(descriptions[1]));
        rowItems3.add(new RowItem(descriptions[2]));
        rowItems3.add(new RowItem(descriptions[3]));


        setContentView(R.layout.activity_pop_over_menu2);
        init();

        listpopupwindowTopLeft = new UIKitListPopupWindow(PopOverMenu.this, topleft, UIKitListPopupWindow.UIKIT_Type.UIKIT_TOPLEFT, rowItems1);
        listpopupwindowTopRight = new UIKitListPopupWindow(PopOverMenu.this,topright, UIKitListPopupWindow.UIKIT_Type.UIKIT_TOPRIGHT, rowItems3);
        listpopupwindowLeft = new UIKitListPopupWindow(PopOverMenu.this, left, UIKitListPopupWindow.UIKIT_Type.UIKIT_LEFT, rowItems1);
        listpopupwindowRight = new UIKitListPopupWindow(PopOverMenu.this, right, UIKitListPopupWindow.UIKIT_Type.UIKIT_RIGHT, rowItems2);
        listpopupwindowBottomLeft = new UIKitListPopupWindow(PopOverMenu.this, buttomleft, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMLEFT, rowItems1);
        listpopupwindowBottomRight = new UIKitListPopupWindow(PopOverMenu.this,buttomright, UIKitListPopupWindow.UIKIT_Type.UIKIT_BOTTOMRIGHT, rowItems1);



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

        if (savedInstanceState != null) {
            type = (Type)savedInstanceState.getSerializable("TYPE");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     if (type != null)
        switch (type) {
            case TOPLEFT:
                topleft.post(new Runnable() {
                    @Override
                    public void run() {
                        listpopupwindowTopLeft.show();
                    }
                });
                break;
            case TOPRIGHT:
                topright.post(new Runnable() {
                    @Override
                    public void run() {
                        listpopupwindowTopRight.show();
                    }
                });
                break;
            case LEFT:
                left.post(new Runnable() {
                    @Override
                    public void run() {
                            listpopupwindowLeft.show();
                    }
                });
                break;
            case RIGHT:
                right.post(new Runnable() {
                    @Override
                    public void run() {
                        listpopupwindowRight.show();
                    }
                });
                break;
            case BOTTOMLEFT:
                buttomleft.post(new Runnable() {
                    @Override
                    public void run() {
                        listpopupwindowBottomLeft.show();
                    }
                });
                break;
            case BOTTOMRIGHT:
                buttomright.post(new Runnable() {
                    @Override
                    public void run() {
                        listpopupwindowBottomRight.show();
                    }
                });
                break;
            default:
        }
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
    protected void onSaveInstanceState(final Bundle outState) {
            super.onSaveInstanceState(outState);


            type = null;
            if (listpopupwindowTopLeft.isShowing()) {
                type = Type.TOPLEFT;
            }else if (listpopupwindowTopRight.isShowing()) {
                type = Type.TOPRIGHT;
            }else if (listpopupwindowLeft.isShowing()) {
                type = Type.LEFT;
            }else if (listpopupwindowRight.isShowing()) {
                type = Type.RIGHT;
            }else if (listpopupwindowBottomLeft.isShowing()) {
                type = Type.BOTTOMLEFT;
            }else if (listpopupwindowBottomRight.isShowing()) {
                type = Type.BOTTOMRIGHT;
            }
        outState.putSerializable("TYPE",type);
    }

    public enum Type {
        TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT
    }
}
