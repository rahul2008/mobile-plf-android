package com.philips.cdp.di.iap.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.Fragments.BaseParentFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class MainActivity extends BaseFragmentActivity {
    TextView mTitleTextView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        IAPLog.d(IAPLog.LOG, "OnCreate");
        setContentView(R.layout.activity_main);
        addActionBar();
        addFragmentAndRemoveUnderneath(new ShoppingCartFragment(), false);
    }

    private void addActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        IAPLog.d(IAPLog.BASE_FRAGMENT_ACTIVITY, "BaseFragmentActivity == onCreate");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.iap_action_bar, null); // layout which contains your button.

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);
        // mTitleTextView.setText(getString(R.string.iap_shopping_cart));

        backButton = (ImageView) mCustomView.findViewById(R.id.arrow);
        backButton.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_up_arrow));

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mActionBar.setCustomView(mCustomView, params);
        mActionBar.setDisplayShowCustomEnabled(true);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    public void setHeaderTitle(String pTitle) {
        mTitleTextView.setText(pTitle);
    }

    public void setHeaderTitle(int pTitle) {
        mTitleTextView.setText(pTitle);
    }

    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    public void hideBackButtonIfNoMoreSubfragments() {
        if (!canRemoveBackbutton())
            return;

        backButton.setVisibility(View.VISIBLE);
    }

    private boolean canRemoveBackbutton() {
        Fragment topFragment = getTopFragment();
        if (topFragment == null || !(topFragment instanceof BaseParentFragment))
            return true;

        return ((BaseParentFragment) topFragment).canRemoveBackButton();
    }
}
