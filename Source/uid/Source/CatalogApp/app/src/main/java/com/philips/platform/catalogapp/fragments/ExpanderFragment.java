/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentExpanderBinding;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.view.widget.Expander;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIDExpanderListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class ExpanderFragment extends BaseFragment {
    UIDExpanderListener mUidExpanderListener = new UIDExpanderListener() {

        @Override
        public void expanderPanelExpanded(Expander expander) {
            Log.v("Expand", "expanderPanelExpanded");
            collapseOtherExpanders(expander);
        }

        @Override
        public void expanderPanelCollapsed(Expander expander) {
            Log.v("Expand", "expanderPanelCollapsed");
        }
    };


    private FragmentExpanderBinding fragmentExpanderBinding;
    private ArrayList<Expander> gallerayExpanderList;


    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentExpanderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expander, container, false);
        fragmentExpanderBinding.setFrag(this);
        return fragmentExpanderBinding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Expander 1
        fragmentExpanderBinding.catalogExpanderOne.setExpanderTitle("Single line title");
        fragmentExpanderBinding.catalogExpanderOne.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderOne.setExpanderListener(mUidExpanderListener);

        //Expander 2
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderTitle("Single line title");
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderTwo.setExpanderListener(mUidExpanderListener);


        //Expander 3
        fragmentExpanderBinding.catalogExpanderThree.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderThree.getTitleLabel().setText("Multiple line title which a bit longer");
        fragmentExpanderBinding.catalogExpanderThree.setExpanderListener(mUidExpanderListener);


        // expander 4
        fragmentExpanderBinding.catalogExpanderFour.setExpanderTitle("Multiple line title which a bit longer in this and can hold additional information");
        RelativeLayout rl = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Label label = new Label(getContext());
        label.setText("customise Expander title");
        RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLabel.addRule(RelativeLayout.CENTER_VERTICAL);
        label.setLayoutParams(paramsLabel);
        rl.addView(label);
        Label label1 = new Label(getContext());
        label1.setText("customise Expander content");
        label1.setLayoutParams(paramsLabel);
        fragmentExpanderBinding.catalogExpanderFour.setExpanderContentView(R.layout.fragment_expander_content_default_layout);
        fragmentExpanderBinding.catalogExpanderFour.setExpanderListener(mUidExpanderListener);


        gallerayExpanderList = new ArrayList<Expander>();
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderOne);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderTwo);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderThree);
        gallerayExpanderList.add(fragmentExpanderBinding.catalogExpanderFour);

        enableIcon(false);
    }

    public void enableIcon(boolean enabled) {
        if (enabled) {
            TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{com.philips.platform.uid.R.attr.uidContentItemSecondaryNormalIconColor});
            int color = typedArray.getColor(0, 0);
            typedArray.recycle();

            FontIconDrawable drawableStar = new FontIconDrawable(getContext(), getResources().getString(com.philips.platform.uid.R.string.dls_star), Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf"))
                    .color(color)
                    .sizeDp(24);

            FontIconDrawable drawablePersonPortrait = new FontIconDrawable(getContext(), getResources().getString(com.philips.platform.uid.R.string.dls_personportrait), Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf"))
                    .color(color)
                    .sizeDp(24);

            FontIconDrawable drawableBalloonSpeech = new FontIconDrawable(getContext(), getResources().getString(com.philips.platform.uid.R.string.dls_balloonspeech), Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf"))
                    .color(color)
                    .sizeDp(24);


            FontIconDrawable drawableCalendar = new FontIconDrawable(getContext(), getResources().getString(com.philips.platform.uid.R.string.dls_calendar), Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf"))
                    .color(color)
                    .sizeDp(24);

            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(drawableStar);
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(drawablePersonPortrait);
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(drawableBalloonSpeech);
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(drawableCalendar);

            /*
            Sample code to demonstarte setting normal drawable
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(getResources().getDrawable(R.drawable.ic_bottle,getActivity().getTheme()));*/

        } else {
            fragmentExpanderBinding.catalogExpanderOne.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderTwo.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderThree.setExpanderPanelIcon(null);
            fragmentExpanderBinding.catalogExpanderFour.setExpanderPanelIcon(null);
        }
    }

    public void openMultiline(boolean enabled) {
        if (!enabled) { // only collapse
            fragmentExpanderBinding.catalogExpanderOne.expand(enabled);
            fragmentExpanderBinding.catalogExpanderTwo.expand(enabled);
            fragmentExpanderBinding.catalogExpanderThree.expand(enabled);
            fragmentExpanderBinding.catalogExpanderFour.expand(enabled);
        }
    }

    private void collapseOtherExpanders(Expander aExpander) {
        if (!fragmentExpanderBinding.toggleIconOpenMultipleItems.isChecked()) {
            for (Expander expander : gallerayExpanderList) {
                if (!expander.equals(aExpander)) {
                    expander.expand(false);
                }
            }
        }

    }
}
