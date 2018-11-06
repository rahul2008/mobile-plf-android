/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw.utils;

import android.text.Spannable;

import com.philips.platform.csw.permission.uielement.LinkSpan;
import com.philips.platform.csw.permission.uielement.LinkSpanClickListener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SpannableHelperTest {

    @Test
    public void getSpannableReturnsReplacingLinkCharacters() {
        String text = "my text {link} some extra text";
        LinkSpanClickListener spanClickListener = new LinkSpanClickListener() {
            @Override
            public void onClick() {
            }
        };

        Spannable spannable = SpannableHelper.getSpannable(text, "{", "}", spanClickListener);

        assertEquals(spannable.toString(),"my text link some extra text");
    }

    @Test
    public void getSpannableReturnsLinkWithinCharacters(){
        String text = "my text {link} some extra text";
        LinkSpanClickListener spanClickListener = new LinkSpanClickListener() {
            @Override
            public void onClick() {
            }
        };

        Spannable spannable = SpannableHelper.getSpannable(text, "{", "}", spanClickListener);

        LinkSpan[] spans = spannable.getSpans(0, spannable.length(), LinkSpan.class);
        assertEquals(spannable.getSpanStart(spans[0]),8);
        assertEquals(spannable.getSpanEnd(spans[0]),12);
    }

    @Test
    public void getSpannableReturnsWholeStringAsLinkIfStartCharacterIsNotFound(){
        String text = "my text link} some extra text";
        LinkSpanClickListener spanClickListener = new LinkSpanClickListener() {
            @Override
            public void onClick() {
            }
        };

        Spannable spannable = SpannableHelper.getSpannable(text, "{", "}", spanClickListener);

        LinkSpan[] spans = spannable.getSpans(0, spannable.length(), LinkSpan.class);
        assertEquals(spannable.getSpanStart(spans[0]),0);
        assertEquals(spannable.getSpanEnd(spans[0]),spannable.length());
    }

    @Test
    public void getSpannableReturnsWholeStringAsLinkIfEndCharacterIsNotFound(){
        String text = "my text {link some extra text";
        LinkSpanClickListener spanClickListener = new LinkSpanClickListener() {
            @Override
            public void onClick() {
            }
        };

        Spannable spannable = SpannableHelper.getSpannable(text, "{", "}", spanClickListener);

        LinkSpan[] spans = spannable.getSpans(0, spannable.length(), LinkSpan.class);
        assertEquals(spannable.getSpanStart(spans[0]),0);
        assertEquals(spannable.getSpanEnd(spans[0]),spannable.length());
    }

    @Test
    public void getSpannableReturnsWholeStringAsLinkIfNoCharactersFound(){
        String text = "my text link some extra text";
        LinkSpanClickListener spanClickListener = new LinkSpanClickListener() {
            @Override
            public void onClick() {
            }
        };

        Spannable spannable = SpannableHelper.getSpannable(text, "{", "}", spanClickListener);

        LinkSpan[] spans = spannable.getSpans(0, spannable.length(), LinkSpan.class);
        assertEquals(spannable.getSpanStart(spans[0]),0);
        assertEquals(spannable.getSpanEnd(spans[0]),spannable.length() - 1);
    }
}