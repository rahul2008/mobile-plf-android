/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.prodreg.constants.EnhancedLinkMovementMethod;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;

import java.util.List;

public class ProdRegConnectionFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegConnectionFragment.class.getName();
    private List<RegisteredProduct> registeredProducts;

    @Override
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return registeredProducts;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_connection, container, false);
        Button backButton = (Button) view.findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                handleBackEvent();
            }
        });
        TextView tv = (TextView) view.findViewById(R.id.link_tv);
        // Linkify the TextView
        Spannable spannable = new SpannableString(Html.fromHtml((String) tv.getText()));
        Linkify.addLinks(spannable, Linkify.WEB_URLS);

        // Replace each URLSpan by a LinkSpan
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan urlSpan : spans) {
            LinkSpan linkSpan = new LinkSpan(urlSpan.getURL());
            int spanStart = spannable.getSpanStart(urlSpan);
            int spanEnd = spannable.getSpanEnd(urlSpan);
            spannable.setSpan(linkSpan, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.removeSpan(urlSpan);
        }

        // Make sure the TextView supports clicking on Links
        tv.setMovementMethod(EnhancedLinkMovementMethod.getInstance());
        tv.setText(spannable, TextView.BufferType.SPANNABLE);
        return view;
    }

    @SuppressWarnings("noinspection unchecked")
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            registeredProducts = (List<RegisteredProduct>) bundle.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
        }
    }

    @Override
    public boolean handleBackEvent() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return clearFragmentStack(true);
        }
        return true;
    }

    private class LinkSpan extends URLSpan {
        private LinkSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View view) {
            String url = getURL();
            ProdRegLogger.v(TAG, "Opening Url in webview");
            final ProdRegWebViewFragment processRegWebViewFragment = new ProdRegWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ProdRegConstants.WEB_URL, url);
            processRegWebViewFragment.setArguments(bundle);
            showFragment(processRegWebViewFragment);
        }
    }
}
