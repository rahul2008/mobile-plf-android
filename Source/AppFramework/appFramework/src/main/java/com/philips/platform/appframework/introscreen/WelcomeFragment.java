/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
public class WelcomeFragment extends Fragment {
    private static final int PAGE_ONE = 0;
    private static final int PAGE_TWO = 1;
    private static final int PAGE_THREE = 2;
    // Store instance variables
    private int page;
    private TextView smallText;

    public static WelcomeFragment newInstance(int page, String title) {
        WelcomeFragment fragmentFirst = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.PAGE_INDEX, page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_welcome_fragment, null);
        smallText = (TextView) view.findViewById(R.id.small_text);
        switch (page) {
            case PAGE_ONE:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.af_welcome_start_page_bg));
                break;
            case PAGE_TWO:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.af_welcome_center_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_two_bottom_text));
                break;
            case PAGE_THREE:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.af_welcome_end_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_three_bottom_text));
                break;
            default:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.af_welcome_start_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_one_bottom_text));
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(Constants.PAGE_INDEX, 0);
    }
}
