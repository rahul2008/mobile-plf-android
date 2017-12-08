package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.TabUtils;
import com.philips.cdp.uikit.utils.UikitUtils;

/**
 * <b></b> ActionBarLauncher is class to demonstrate the use of Action Up Button </b>
 * <p/>
 * <p/>
 * <b></b>Inorder to use Make use of this, infalte the custom Layout (uikit_action_bar.xml) to the Android default layout</b><br>
 * <pre>
 * ActionBar mActionBar = this.getSupportActionBar();
 * mActionBar.setDisplayShowHomeEnabled(false);
 * mActionBar.setDisplayShowTitleEnabled(false);
 * ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
 * ActionBar.LayoutParams.MATCH_PARENT,
 * ActionBar.LayoutParams.WRAP_CONTENT,
 * Gravity.CENTER);
 * View mCustomView = LayoutInflater.from(this).inflate(R.layout.uikit_action_bar, null); // layout which contains your button.
 * mActionBar.setCustomView(mCustomView, params);
 * mActionBar.setDisplayShowCustomEnabled(true);
 * </pre>
 */
public class ActionBarLauncher extends CatalogActivity {

    /**
     * Get the ActionBar and inflate Custom Action Bar and also set onClickListener for the arrow Button
     * <li>Infalte uikit_action_bar.xml to the Android default Action Bar</li>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.uikit_action_bar, null); // layout which contains your button.

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });

        ImageView arrowImage = (ImageView) mCustomView
                .findViewById(R.id.arrow);
        arrowImage.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_up_arrow));

        mActionBar.setCustomView(mCustomView, params);
        mActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        setContentView(R.layout.action_bar_hide_scroll);
        mActionBar.setHideOnContentScrollEnabled(true);
        TabUtils.disableActionbarShadow(this);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pop_over_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        UikitUtils.menuShowIcon(menu);
        menu.getItem(0).setIcon(VectorDrawable.create(this, R.drawable.uikit_gear_19_19));
        menu.getItem(1).setIcon(VectorDrawable.create(this, R.drawable.uikit_share_19_18));
        menu.getItem(2).setIcon(VectorDrawable.create(this, R.drawable.uikit_envelope));
        menu.getItem(3).setIcon(VectorDrawable.create(this, R.drawable.uikit_ballon));

        return super.onPrepareOptionsMenu(menu);

    }
}
