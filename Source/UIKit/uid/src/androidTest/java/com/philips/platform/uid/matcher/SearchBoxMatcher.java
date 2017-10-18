/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.matcher;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.view.widget.SearchBox;

import org.hamcrest.Matcher;

public class SearchBoxMatcher {

    public static Matcher<View> isSameBackIconEndMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                AppCompatAutoCompleteTextView textView = searchBox.getSearchTextView();
                return ViewPropertiesMatchers.isSameStartMargin(expectedValue).matches(textView);
            }
        };
    }

    public static Matcher<View> isSameBackIconStartMargin(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getCollapseView();
                return ViewPropertiesMatchers.isSameStartMargin(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameBackIconHeight(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getCollapseView();
                return ViewPropertiesMatchers.isSameViewHeight(expectedValue).matches(imageView);
            }
        };
    }

    public static Matcher<View> isSameBackIconWidth(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView imageView = searchBox.getCollapseView();
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
                return TextViewPropertiesMatchers.isSameTypeface(activity, fontPath).matches(textView);
            }
        };
    }

    public static Matcher<View> isSameTextColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View textView = searchBox.getSearchTextView();
                return TextViewPropertiesMatchers.isSameTextColor(expectedValue).matches(textView);
            }
        };
    }

    public static Matcher<View> isSameHintTextColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                View textView = searchBox.getSearchTextView();
                return TextViewPropertiesMatchers.isSameHintTextColor(android.R.attr.state_enabled, expectedValue).matches(textView);
            }
        };
    }

    public static Matcher<View> isSameFillColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                LinearLayout searchBoxViewById = (LinearLayout) searchBox.findViewById(com.philips.platform.uid.test.R.id.uid_search_box_layout);
                return ViewPropertiesMatchers.hasSameColorDrawableBackgroundColor(expectedValue).matches(searchBoxViewById);
            }
        };
    }

    public static Matcher<View> isSameIconColor(final int expectedValue) {
        return new BaseTypeSafteyMatcher<View>() {

            @Override
            protected boolean matchesSafely(View view) {
                SearchBox searchBox = (SearchBox) view;
                ImageView backIcon = searchBox.getCollapseView();
                Drawable drawable = backIcon.getDrawable();
                return DrawableMatcher.isSameStrokeColor(new int[android.R.attr.state_enabled], expectedValue).matches(drawable);
            }
        };
    }
}
