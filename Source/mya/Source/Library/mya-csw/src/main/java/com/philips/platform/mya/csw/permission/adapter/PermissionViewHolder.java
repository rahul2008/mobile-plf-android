/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission.adapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.csw.permission.ConsentToggleListener;
import com.philips.platform.mya.csw.permission.ConsentView;
import com.philips.platform.mya.csw.permission.HelpClickListener;
import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

class PermissionViewHolder extends BasePermissionViewHolder {

    private Switch toggle;
    private Label label;
    private Label help;
    private Label error;
    @Nullable
    private ConsentToggleListener consentToggleListener;
    @NonNull
    private HelpClickListener helpClickListener;

    private int heilightColorCode;
    private int deafaultColorCode;

    PermissionViewHolder(@NonNull View itemView, int parentWidth, @NonNull HelpClickListener helpClickListener, @Nullable ConsentToggleListener consentToggleListener) {
        super(itemView, parentWidth);
        this.toggle = itemView.findViewById(R.id.toggleicon);
        this.label = itemView.findViewById(R.id.consentText);
        this.help = itemView.findViewById(R.id.consentHelp);
        this.error = itemView.findViewById(R.id.consentError);
        this.consentToggleListener = consentToggleListener;
        this.helpClickListener = helpClickListener;
        this.help.setPaintFlags(this.help.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        this.heilightColorCode = ContextCompat.getColor(itemView.getContext(), com.philips.cdp.registration.R.color.reg_hyperlink_highlight_color);
        this.deafaultColorCode = ContextCompat.getColor(itemView.getContext(), android.R.color.transparent);
    }

    void setDefinition(final ConsentView consentView) {
        // Update UI here
        label.setText(consentView.getConsentText());
        error.setVisibility(consentView.isError() ? View.VISIBLE : View.INVISIBLE);
        toggle.animate().alpha(consentView.isError() ? 0.5f : 1.0f).start();
        toggle.setEnabled(consentView.isEnabled());
        toggle.setChecked(consentView.isChecked());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final ConsentHandlerInterface handler = consentView.getHandler();
                if (consentToggleListener != null && handler != null) {
                    setLoading(consentView);
                    consentToggleListener.onToggledConsent(consentView.getDefinition(), handler, b);
                }
            }
        });
        linkify(help, new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                helpClickListener.onHelpClicked(consentView.getHelpText());
            }
        });
    }

    @Override
    public void onViewRecycled() {
        this.toggle.setOnCheckedChangeListener(null);
    }

    private void setLoading(ConsentView consentView) {
        consentView.setIsLoading(true);
        consentView.setError(false);
        error.setVisibility(View.INVISIBLE);
    }

    private void linkify(TextView pTvPrivacyPolicy, ClickableSpan span) {
        String privacy = pTvPrivacyPolicy.getText().toString();
        SpannableString spannableString = new SpannableString(privacy);

        spannableString.setSpan(span, 0, privacy.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        removeUnderlineFromLink(spannableString);

        pTvPrivacyPolicy.setText(spannableString);
        pTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        pTvPrivacyPolicy.setLinkTextColor(heilightColorCode);
        pTvPrivacyPolicy.setHighlightColor(deafaultColorCode);
    }

    private static void removeUnderlineFromLink(SpannableString spanableString) {
        for (ClickableSpan u : spanableString.getSpans(0, spanableString.length(),
                ClickableSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }

        for (URLSpan u : spanableString.getSpans(0, spanableString.length(), URLSpan.class)) {
            spanableString.setSpan(new UnderlineSpan() {

                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
        }
    }
}
