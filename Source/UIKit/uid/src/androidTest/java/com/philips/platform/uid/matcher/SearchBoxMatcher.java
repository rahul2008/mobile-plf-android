/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.philips.platform.uid.view.widget.SearchBox;

import org.hamcrest.Matcher;

public class SearchBoxMatcher {

    public static Matcher<View> isSameBackIconEndMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getBackButton();
                return ViewPropertiesMatchers.isSameEndMargin(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameBackIconStartMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getBackButton();
                return ViewPropertiesMatchers.isSameStartMargin(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameBackIconHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getBackButton();
                return ViewPropertiesMatchers.isSameViewHeight(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameBackIconWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getBackButton();
                return ViewPropertiesMatchers.isSameViewWidth(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameClearIconHolderWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View viewHolder = searchBox.getSearchClearLayout();
                return ViewPropertiesMatchers.isSameViewWidth(expectedValue).matches(viewHolder);
            }
        };
    }

    public static Matcher<View> isSameClearIconHolderHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View viewHolder = searchBox.getSearchClearLayout();
                return ViewPropertiesMatchers.isSameViewHeight(expectedValue).matches(viewHolder);
            }
        };
    }

    public static Matcher<View> isSameClearIconStartMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View viewHolder = searchBox.getSearchClearLayout();
                return ViewPropertiesMatchers.isSameStartMargin(expectedValue).matches(viewHolder);
            }
        };
    }

    public static Matcher<View> isSameClearIconEndMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View viewHolder = searchBox.getSearchClearLayout();
                return ViewPropertiesMatchers.isSameEndMargin(expectedValue).matches(viewHolder);
            }
        };
    }

    public static Matcher<View> isSameTextSize(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View textView = searchBox.getSearchTextView();
                return TextViewPropertiesMatchers.isSameFontSize(expectedValue).matches(textView);
            }
        };
    }

    public static Matcher<View> isSameTypeFace(final Context activity, final String fontPath) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                AutoCompleteTextView textView = searchBox.getSearchTextView();
                return TextViewPropertiesMatchers.isSameTypeface(activity, textView.getTypeface(), fontPath).matches(textView);
            }
        };
    }
}
