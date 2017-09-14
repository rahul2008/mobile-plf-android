/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uid.matcher;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import org.hamcrest.Matcher;

public class DrawerMatcher {

    public static Matcher<? super View> isSameDrawerElevation(final float elevation) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DrawerLayout) {
                    DrawerLayout drawerLayout = (DrawerLayout) view;
                    setValues(drawerLayout.getDrawerElevation(), elevation);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isSameDrawerHeight(final float deviceHeight) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DrawerLayout) {
                    DrawerLayout drawerLayout = (DrawerLayout) view;
                    float actualHeight = drawerLayout.getHeight();
                    setValues(actualHeight, deviceHeight);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isDrawerFollowMaxWidth(final float maxWidth) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DrawerLayout) {
                    DrawerLayout drawerLayout = (DrawerLayout) view;
                    float actualWidth = drawerLayout.getChildAt(1).getWidth();
                    setValues(actualWidth, maxWidth);
                    return actualWidth <= maxWidth;
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isDrawerClose(final int gravityCompat) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DrawerLayout) {
                    DrawerLayout drawerLayout = (DrawerLayout) view;
                    setValues(!(drawerLayout.isDrawerOpen(gravityCompat)), true);
                    return areEqual();
                }
                return false;
            }
        };
    }

    public static Matcher<? super View> isDrawerOpen(final int gravityCompat) {
        return new BaseTypeSafteyMatcher<View>() {
            @Override
            protected boolean matchesSafely(final View view) {
                if (view instanceof DrawerLayout) {
                    DrawerLayout drawerLayout = (DrawerLayout) view;
                    setValues(drawerLayout.isDrawerOpen(gravityCompat), true);
                    return areEqual();
                }
                return false;
            }
        };
    }
}