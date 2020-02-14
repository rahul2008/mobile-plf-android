/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.pager;

import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uid.view.widget.Label;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
public class WelcomePagerFragment extends AbstractAppFrameworkBaseFragment {
    public static final String TAG = WelcomePagerFragment.class.getSimpleName();

    private static final String ARG_PAGE_TITLE = "pageTitle";
    private static final String ARG_PAGE_SUBTITLE = "pageSubtitle";
    private static final String ARG_PAGE_BG_ID = "pageBgId";
    private static final String ARG_PAGE_GROUP_ID = "pageGrpId";


    // Store instance variables
    @StringRes
    private int titleId;
    @StringRes
    private int subtitleId;
    @DrawableRes
    private int backgroundId;
    @DrawableRes
    private int groupId;

    @BindView(R.id.background_image)
    ImageView backgroundimg;

    @BindView(R.id.welcome_slide_fragment_layout)
    LinearLayout llBottomParent;

    @BindView(R.id.group_image)
    ImageView groupView;

    @BindView(R.id.welcome_slide_small_text)
    Label smallText;

    public static WelcomePagerFragment newInstance(@StringRes int title, @StringRes int subtitle,
                                                   @DrawableRes int background, @DrawableRes int group) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TITLE, title);
        args.putInt(ARG_PAGE_SUBTITLE, subtitle);
        args.putInt(ARG_PAGE_BG_ID, background);
        args.putInt(ARG_PAGE_GROUP_ID, group);
        WelcomePagerFragment fragmentFirst = new WelcomePagerFragment();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        RALog.d(TAG, "WelcomePagerFragment onCreate");
        super.onCreate(savedInstanceState);
        if(!(savedInstanceState!=null && !AppFrameworkApplication.isAppDataInitialized())) {
            titleId = getArguments().getInt(ARG_PAGE_TITLE, 0);
            subtitleId = getArguments().getInt(ARG_PAGE_SUBTITLE, 0);
            backgroundId = getArguments().getInt(ARG_PAGE_BG_ID, R.drawable.onboarding_02);
            groupId = getArguments().getInt(ARG_PAGE_GROUP_ID, R.drawable.group1);
        }

    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_welcome_slide_fragment, null);
        ButterKnife.bind(this, view);
        initView(view);
        updateDrawables(getResources().getConfiguration().orientation);
        return view;
    }

    private void updateDrawables(int orientation) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) groupView.getLayoutParams();
        layoutParams.setMargins(0, size.y / 6, 0 , 0);
        groupView.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams bottomLayoutParams = (RelativeLayout.LayoutParams) llBottomParent.getLayoutParams();
        bottomLayoutParams.setMargins(0, 0, 0 , size.y / 9);
        llBottomParent.setLayoutParams(bottomLayoutParams);
        backgroundimg.setImageDrawable(getResources().getDrawable(backgroundId, getActivity().getTheme()));

    }

    public void initView(View view) {
        Label largeText = (Label) view.findViewById(R.id.welcome_slide_large_text);
        largeText.setText(titleId);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            smallText.setText(Html.fromHtml(getString(subtitleId), Html.FROM_HTML_MODE_LEGACY));
        } else {
            smallText.setText(Html.fromHtml(getString(subtitleId)));
        }
        groupView.setImageResource(groupId);
    }


}