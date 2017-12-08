package com.philips.cdp.uikit.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.philips.cdp.uikit.R;
import com.shamanland.fonticon.FontIconDrawable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FontIconUtils {
    public enum ICONS {
        TICK_LIGHT(R.string.uikit_fonticon_tick_light),
        EXCLAMATION(R.string.uikit_fonticon_exclamation),
        HEART(R.string.uikit_fonticon_heart),
        SHIELD(R.string.uikit_fonticon_shield),
        QUESTION(R.string.uikit_fonticon_question),
        FACEBOOK(R.string.uikit_fonticon_facebook),
        GOOGLE_PLUS(R.string.uikit_fonticon_google_plus),
        LINKEDIN(R.string.uikit_fonticon_linkedin),
        PINTEREST(R.string.uikit_fonticon_pininterest),
        STUMBLE_UPON(R.string.uikit_fonticon_stumble_upon),
        TWITTER(R.string.uikit_fonticon_twitter),
        YOUTUBE(R.string.uikit_fonticon_youtube),
        TICK_BOLD(R.string.uikit_fonticon_tick_bold),
        ARROW_DOWN(R.string.uikit_fonticon_arrow_down),
        ARROW_LEFT(R.string.uikit_fonticon_arrow_left),
        ARROW_RIGHT(R.string.uikit_fonticon_arrow_right),
        ARROW_UP(R.string.uikit_fonticon_arrow_up),
        DASH(R.string.uikit_fonticon_dash),
        STAR(R.string.uikit_fonticon_star),
        PLUS(R.string.uikit_fonticon_plus),
        CROSS(R.string.uikit_fonticon_cross),
        HAMBURGER(R.string.uikit_fonticon_hamburger),
        INFO(R.string.uikit_fonticon_info);

        int resID;

        ICONS(int id) {
            resID = id;
        }

        int getResourceID() {
            return resID;
        }
    }

    /**
     * Create icon from the font file.
     *
     * @param context
     * @param ICONS
     * @param size     size for the icon
     * @param color:   Color of the icon
     * @param scalable : Should respect system font size settings. False applies in dp, true
     *                 applies dimension in sp.
     * @return
     */
    public static Drawable getInfo(Context context, ICONS ICONS, int size, int color, boolean
            scalable) {
        return getDrawable(context, size, ICONS.getResourceID(), color, scalable);
    }

    private static Drawable getDrawable(Context context, int size, int resID, int
            color, boolean scalable) {
        FontIconDrawable drawable = FontIconDrawable.inflate(context, R.xml.uikit_fonticon_base);
        drawable.setText(context.getResources().getString(resID));
        drawable.setTextColor(color);

        int unit = TypedValue.COMPLEX_UNIT_DIP;
        if (scalable) {
            unit = TypedValue.COMPLEX_UNIT_SP;
        }
        float textSize = TypedValue.applyDimension(unit, size, context.getResources()
                .getDisplayMetrics());
        drawable.setTextSize(textSize);
        return drawable;
    }
}