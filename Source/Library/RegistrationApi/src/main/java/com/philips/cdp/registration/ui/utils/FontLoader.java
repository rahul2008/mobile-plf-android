/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.*;

/**
 * Fond loader class for the cutom mFonts.
 */
public class FontLoader {

    private static volatile FontLoader mInstance;

    private final Map<String, Typeface> mFonts;

    private FontLoader() {
        mFonts = new HashMap<>();
    }

    public synchronized static FontLoader getInstance() {
        if (mInstance == null) {
            synchronized (FontLoader.class) {
                if (mInstance == null) {
                    mInstance = new FontLoader();
                }
            }
        }

        return mInstance;
    }

    /**
     * Set type face
     * @param tv {@link TextView}
     * @param fontName
     */
    public void setTypeface(TextView tv, String fontName) {
        String fontTypeFaceName = fontName;
        if (fontTypeFaceName == null || fontTypeFaceName.isEmpty()) {
            return;
        }
        fontTypeFaceName = RegConstants.FONT_PATH + fontTypeFaceName;
        if (!tv.isInEditMode()) {
            Typeface typeface = mFonts.get(fontTypeFaceName);
            if (typeface == null) {
                typeface = Typeface.createFromAsset(tv.getContext().getAssets(), fontTypeFaceName);

                mFonts.put(fontTypeFaceName, typeface);
            }
            tv.setTypeface(typeface);
        }
    }

}
