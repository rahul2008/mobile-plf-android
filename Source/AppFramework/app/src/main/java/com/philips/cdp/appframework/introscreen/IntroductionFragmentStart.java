package com.philips.cdp.appframework.introscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.appframework.R;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

public class IntroductionFragmentStart extends Fragment {
    // Store instance variables
    private int page;
    private TextView largeText, smallText;
    private static final int PAGE_ONE = 0;
    private static final int PAGE_TWO = 1;
    private static final int PAGE_THREE = 2;

    public static IntroductionFragmentStart newInstance(int page, String title) {
        IntroductionFragmentStart fragmentFirst = new IntroductionFragmentStart();
        Bundle args = new Bundle();
        args.putInt("pageIndex", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.introduction_screen_start, null);
        largeText = (TextView) view.findViewById(R.id.large_text);
        smallText = (TextView) view.findViewById(R.id.small_text);
        switch (page) {
            case PAGE_ONE:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.introduction_start_page_bg));
                break;
            case PAGE_TWO:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.introduction_center_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_two_bottom_text));
                break;
            case PAGE_THREE:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.introduction_end_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_three_bottom_text));
                break;
            default:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
                        ContextCompat.getDrawable(getActivity(), R.drawable.introduction_start_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_one_bottom_text));
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("pageIndex", 0);
    }
}
