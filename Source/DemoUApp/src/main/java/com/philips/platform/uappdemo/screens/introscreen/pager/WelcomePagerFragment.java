/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.uappdemo.screens.introscreen.pager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uappdemolibrary.R;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
public class WelcomePagerFragment extends Fragment {

    private static final String ARG_PAGE_TITLE = "pageTitle";
    private static final String ARG_PAGE_SUBTITLE = "pageSubtitle";
    private static final String ARG_PAGE_BG_ID = "pageBgId";

    // Store instance variables
    @StringRes
    private int titleId;
    @StringRes
    private int subtitleId;
    @DrawableRes
    private int backgroundId;

    public static WelcomePagerFragment newInstance(@StringRes int title, @StringRes int subtitle,
                                                   @DrawableRes int background) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TITLE, title);
        args.putInt(ARG_PAGE_SUBTITLE, subtitle);
        args.putInt(ARG_PAGE_BG_ID, background);

        WelcomePagerFragment fragmentFirst = new WelcomePagerFragment();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleId = getArguments().getInt(ARG_PAGE_TITLE, 0);
        subtitleId = getArguments().getInt(ARG_PAGE_SUBTITLE, 0);
        backgroundId = getArguments().getInt(ARG_PAGE_BG_ID, R.drawable.af_welcome_start_page_bg);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_welcome_slide_fragment, null);

        TextView largeText = (TextView) view.findViewById(R.id.welcome_slide_large_text);
        TextView smallText = (TextView) view.findViewById(R.id.welcome_slide_small_text);
        View background = view.findViewById(R.id.welcome_slide_fragment_layout);

        largeText.setText(titleId);
        smallText.setText(subtitleId);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getActivity().getResources(), decodeSampledBitmapFromResource(getActivity().getResources(), backgroundId, widthPixels/4,heightPixels/4));
        background.setBackground(bitmapDrawable);

        return view;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}