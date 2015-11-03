package com.philips.cdp.uikit;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitActivity extends AppCompatActivity {

    private TextView actionBarTitle;
    private ActionBar actionBar;

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initActionBar(getSupportActionBar());
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/centralesans_book.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (validateHamburger()) {
            actionBar.setHomeAsUpIndicator(VectorDrawable.create(this, R.drawable.uikit_hamburger_icon));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (validateHamburger()) {
            getMenuInflater().inflate(R.menu.uikit_hamburger_menu_item, menu);
            MenuItem menuItem = menu.findItem(R.id.action_reload);
            inflateVectorMenu(menuItem);
            return true;
        }
        return super.onCreateOptionsMenu(menu);

    }

    private boolean validateHamburger() {
        if (findViewById(R.id.philips_drawer_layout) != null)
            return true;
        else
            return false;
    }

    private void inflateVectorMenu(MenuItem menuItem) {
        VectorDrawableImageView vectorDrawableImageView = new VectorDrawableImageView(this);
        vectorDrawableImageView.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_reload));
        menuItem.setActionView(vectorDrawableImageView);
    }

    private void initActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.hamburger_title);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (actionBarTitle != null)
            actionBarTitle.setText(title);
    }

}
