package com.philips.cdp.digitalcare.Utility.test;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * This class is the Dynamic View to test the Fragments used in the projects.
 *
 * @author naveen@philips.com
 * @since 26/Aug/2015
 */
public class Container {

    public static final int CONTAINER_ID = 100 * 1;
    public static final int CONTAINER_FRAGMENT = 100 * 2;
    public static final int CONTAINER_ACTIONBAR = 100 * 3;
    public static final int CONTAINER_ICON_HOME = 100 * 4;
    public static final int CONTAINER_ICON_BACK = 100 * 5;
    public static final int CONTAINER_ICON_TEXT = 100 * 6;

    RelativeLayout mActionBarContainer = null;
    RelativeLayout mIconback = null;
    RelativeLayout mHome = null;
    com.philips.cdp.digitalcare.customview.DigitalCareFontTextView mTextView = null;

    FrameLayout mFragmentParent = null;
    RelativeLayout mFragmentContainer = null;

    private Context mContext = null;


    private FrameLayout getFragmentParentContainer() {
        mFragmentParent = new FrameLayout(mContext);
        mFragmentParent.setId(CONTAINER_ID);
        mFragmentParent.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        return mFragmentParent;
    }


    private View getFragmentContainer() {
        mFragmentContainer = new RelativeLayout(mContext);
        mFragmentContainer.setId(CONTAINER_FRAGMENT);
        mFragmentContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        return mFragmentContainer;
    }

    public View getLayout() {
        FrameLayout mLayout = getFragmentParentContainer();
        mLayout.addView(getActionBarTitle());
        mLayout.addView(getFragmentContainer());
        return mLayout;
    }


    public View getActionBarTitle() {
        mActionBarContainer = new RelativeLayout(mContext);
        mActionBarContainer.setId(CONTAINER_ACTIONBAR);
        mActionBarContainer.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));

        mIconback = new RelativeLayout(mContext);
        mIconback.setId(CONTAINER_ICON_BACK);
        mIconback.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));

        mHome = new RelativeLayout(mContext);
        mHome.setId(CONTAINER_ICON_HOME);
        mHome.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));

        mTextView = new com.philips.cdp.digitalcare.customview.DigitalCareFontTextView(mContext);
        mTextView.setId(CONTAINER_ICON_TEXT);
        mTextView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));


        mActionBarContainer.addView(mIconback);
        mActionBarContainer.addView(mHome);
        mActionBarContainer.addView(mTextView);

        return mActionBarContainer;
    }


}
