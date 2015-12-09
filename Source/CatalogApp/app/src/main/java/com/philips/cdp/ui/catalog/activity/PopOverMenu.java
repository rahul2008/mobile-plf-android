package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UIKitListPopupWindow;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.CustomListViewAdapter;
import com.philips.cdp.uikit.utils.RowItem;

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
            "Add to favs",
            "Share", "Mail",
            "Chat" };

    public   Integer[] images = {R.drawable.uikit_apple, R.drawable.uikit_share,
            R.drawable.uikit_envelope,R.drawable.uikit_ballon, };

    List<RowItem> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        rowItems = new ArrayList<RowItem>();

        RowItem item1 = new RowItem();
        item1.setDesc(descriptions[0]);
        /*item1.setDrawable(FontIconUtils.getInfo(this, FontIconUtils.ICONS.HEART, 22, Color.WHITE,
                false));*/
        item1.setDrawable(VectorDrawable.create(this, R.drawable.uikit_share));

        rowItems.add(item1);


        RowItem item2 = new RowItem();
        item2.setDesc(descriptions[1]);
        item2.setDrawable(VectorDrawable.create(this, R.drawable.uikit_share));
        rowItems.add(item2);

        RowItem item3 = new RowItem();
        item3.setDesc(descriptions[2]);
        item3.setDrawable(VectorDrawable.create(this, R.drawable.uikit_envelope));
        rowItems.add(item3);


        RowItem item4 = new RowItem();
        item4.setDesc(descriptions[3]);
        item4.setDrawable(VectorDrawable.create(this, R.drawable.uikit_ballon));
        rowItems.add(item4);









        setContentView(R.layout.activity_pop_over_menu2);
        init();
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.simple_list_image_text, rowItems);

        listpopupwindowTopLeft = new UIKitListPopupWindow(PopOverMenu.this, topleft, UIKitListPopupWindow.Type.TOPLEFT);
        listpopupwindowTopLeft.setAdapter(adapter);


        listpopupwindowTopRight = new UIKitListPopupWindow(PopOverMenu.this,topright,UIKitListPopupWindow.Type.TOPRIGHT);
        listpopupwindowTopRight.setAdapter(adapter);

        listpopupwindowLeft = new UIKitListPopupWindow(PopOverMenu.this,left,UIKitListPopupWindow.Type.LEFT);
        listpopupwindowLeft.setAdapter(adapter);

        listpopupwindowRight = new UIKitListPopupWindow(PopOverMenu.this,right,UIKitListPopupWindow.Type.RIGHT);
        listpopupwindowRight.setAdapter(adapter);

        listpopupwindowBottomLeft = new UIKitListPopupWindow(PopOverMenu.this,buttomleft,UIKitListPopupWindow.Type.BOTTOMLEFT);
        listpopupwindowBottomLeft.setAdapter(adapter);

        listpopupwindowBottomRight = new UIKitListPopupWindow(PopOverMenu.this,buttomright,UIKitListPopupWindow.Type.BOTTOMRIGHT);
        listpopupwindowBottomRight.setAdapter(adapter);










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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
