/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.thememanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;

import com.philips.platform.uit.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;

// TODO: 14/09/16 API needs refractoring as some code is copied from AOSP
public class ThemeUtils {

    public static Resources.Theme getTheme(@NonNull final Context context, @NonNull final AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.uitComponentType});

        int resourceId = getResourceIdBasedComponentType(typedArray);
        typedArray.recycle();
        if (resourceId == 0) {
            return context.getTheme();
        }
        final Resources.Theme theme = context.getResources().newTheme();
        theme.setTo(context.getTheme());
        theme.applyStyle(resourceId, true);
        return theme;
    }

    private static int getResourceIdBasedComponentType(@NonNull final TypedArray typedArray) {
        int resourceId = typedArray.getInt(0, 0);
        if (resourceId == 1) {
            resourceId = R.style.UITPrimaryControl_AlternatePrimary;
        } else if (resourceId == 2) {
            resourceId = R.style.UITPrimaryControl_Secondary;
        }

        return resourceId;
    }

    private static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }

    /**
     * @param resources
     * @param theme
     * @param resId
     * @return
     */
    @Nullable
    public static ColorStateList buildColorStateList(Resources resources, Resources.Theme theme, int resId) {

        final XmlPullParser xml = resources.getXml(resId);
        try {
            return createFromXml(resources, xml, theme);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Creates a ColorStateList from an XML document using given a set of
     * {@link Resources} and a {@link Theme}.
     *
     * @param resources Resources against which the ColorStateList should be inflated.
     * @param parser    Parser for the XML document defining the ColorStateList.
     * @param theme     Optional theme to apply to the color state list, may be
     *                  {@code null}.
     * @return A new color state list.
     */
    @NonNull
    public static ColorStateList createFromXml(@NonNull Resources resources, @NonNull XmlPullParser parser,
                                               @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        final AttributeSet attrs = Xml.asAttributeSet(parser);

        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT) {
            // Seek parser to start tag.
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        return createFromXmlInner(resources, parser, attrs, theme);
    }

    /**
     * Create from inside an XML document. Called on a parser positioned at a
     * tag in an XML document, tries to create a ColorStateList from that tag.
     *
     * @return A new color state list for the current tag.
     * @throws XmlPullParserException if the current tag is not &lt;selector>
     */
    @NonNull
    private static ColorStateList createFromXmlInner(@NonNull Resources r,
                                                     @NonNull XmlPullParser parser, @NonNull AttributeSet attrs,
                                                     @Nullable Resources.Theme theme)
            throws XmlPullParserException, IOException {
        final String name = parser.getName();
        if (!name.equals("selector")) {
            throw new XmlPullParserException(
                    parser.getPositionDescription() + ": invalid color state list tag " + name);
        }

        return inflate(r, parser, attrs, theme);
    }

    /**
     * Fill in this object based on the contents of an XML "selector" element.
     */
    private static ColorStateList inflate(@NonNull Resources r, @NonNull XmlPullParser parser,
                                          @NonNull AttributeSet attrs, @Nullable Resources.Theme theme)
            throws XmlPullParserException, IOException {
        final int innerDepth = parser.getDepth() + 1;
        int depth;
        int type;
        int defaultColor = Color.RED;

        int[][] stateSpecList = new int[20][];
        int[] colorList = new int[stateSpecList.length];
        int listSize = 0;

        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                && ((depth = parser.getDepth()) >= innerDepth || type != XmlPullParser.END_TAG)) {
            if (type != XmlPullParser.START_TAG || depth > innerDepth
                    || !parser.getName().equals("item")) {
                continue;
            }

            final TypedArray a = obtainAttributes(r, theme, attrs, android.support.v7.appcompat.R.styleable.ColorStateListItem);
            final int baseColor = a.getColor(android.support.v7.appcompat.R.styleable.ColorStateListItem_android_color,
                    Color.MAGENTA);

            float alphaMod = 1.0f;
            if (a.hasValue(android.support.v7.appcompat.R.styleable.ColorStateListItem_android_alpha)) {
                alphaMod = a.getFloat(android.support.v7.appcompat.R.styleable.ColorStateListItem_android_alpha, alphaMod);
            } else if (a.hasValue(android.support.v7.appcompat.R.styleable.ColorStateListItem_alpha)) {
                alphaMod = a.getFloat(android.support.v7.appcompat.R.styleable.ColorStateListItem_alpha, alphaMod);
            }

            a.recycle();

            // Parse all unrecognized attributes as state specifiers.
            int j = 0;
            final int numAttrs = attrs.getAttributeCount();
            int[] stateSpec = new int[numAttrs];
            for (int i = 0; i < numAttrs; i++) {
                final int stateResId = attrs.getAttributeNameResource(i);
                if (stateResId != android.R.attr.color && stateResId != android.R.attr.alpha
                        && stateResId != android.support.v7.appcompat.R.attr.alpha) {
                    // Unrecognized attribute, add to state set
                    stateSpec[j++] = attrs.getAttributeBooleanValue(i, false)
                            ? stateResId : -stateResId;
                }
            }
            stateSpec = StateSet.trimStateSet(stateSpec, j);

            // Apply alpha modulation. If we couldn't resolve the color or
            // alpha yet, the default values leave us enough information to
            // modulate again during applyTheme().
            final int color = modulateColorAlpha(baseColor, alphaMod);
            if (listSize == 0 || stateSpec.length == 0) {
                defaultColor = color;
            }

            colorList = append(colorList, listSize, color);
            stateSpecList = append(stateSpecList, listSize, stateSpec);
            listSize++;
        }

        int[] colors = new int[listSize];
        int[][] stateSpecs = new int[listSize][];
        System.arraycopy(colorList, 0, colors, 0, listSize);
        System.arraycopy(stateSpecList, 0, stateSpecs, 0, listSize);

        return new ColorStateList(stateSpecs, colors);
    }

    /**
     * Appends an element to the end of the array, growing the array if there is no more room.
     *
     * @param array       The array to which to append the element. This must NOT be null.
     * @param currentSize The number of elements in the array. Must be less than or equal to
     *                    array.length.
     * @param element     The element to append.
     * @return the array to which the element was appended. This may be different than the given
     * array.
     */
    public static <T> T[] append(T[] array, int currentSize, T element) {
        assert currentSize <= array.length;

        if (currentSize + 1 > array.length) {
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(),
                    growSize(currentSize));
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    /**
     * Primitive int version of {@link #append(Object[], int, Object)}.
     */
    public static int[] append(int[] array, int currentSize, int element) {
        assert currentSize <= array.length;

        if (currentSize + 1 > array.length) {
            int[] newArray = new int[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    /**
     * Given the current size of an array, returns an ideal size to which the array should grow.
     * This is typically double the given size, but should not be relied upon to do so in the
     * future.
     */
    public static int growSize(int currentSize) {
        return currentSize <= 4 ? 8 : currentSize * 2;
    }

    private static TypedArray obtainAttributes(Resources res, Resources.Theme theme,
                                               AttributeSet set, int[] attrs) {
        return theme == null ? res.obtainAttributes(set, attrs)
                : theme.obtainStyledAttributes(set, attrs, 0, 0);
    }
}
