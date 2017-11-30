/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import android.content.res.Resources;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PermissionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NOT_FOUND = -1;
    public static final int HEADER_COUNT = 1;
    @NonNull
    private final List<ConsentView> items;
    @NonNull
    private final HelpClickListener helpClickListener;

    @Nullable
    private ConsentToggleListener consentToggleListener;

    public static int heilightColorCode;
    public static int deafaultColorCode;

    @Inject
    PermissionAdapter(@NonNull final List<ConsentView> definitions, @NonNull HelpClickListener helpClickListener) {
        this.items = new ArrayList<>(definitions);
        this.helpClickListener = helpClickListener;
    }

    void setConsentToggleListener(@Nullable ConsentToggleListener consentToggleListener) {
        this.consentToggleListener = consentToggleListener;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_row, parent, false);
            applyParams(view,parent.getWidth());
            heilightColorCode = parent.getContext().getResources().getColor(com.philips.cdp.registration.R.color.reg_hyperlink_highlight_color);
            deafaultColorCode = ContextCompat.getColor(parent.getContext(),
                    android.R.color.transparent);
            return new PermissionViewHolder(view, helpClickListener, consentToggleListener);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_header, parent, false);
            applyParams(view,parent.getWidth());
            return new Header(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }



    private void applyParams(View view, int width) {
        if (width < dpToPx(648)) {
            applyDefaultMargin(view);
        } else {
            setMaxWidth(view);
        }
    }

    private void setMaxWidth(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = dpToPx(648);
        view.setLayoutParams(params);
    }

    private void applyDefaultMargin(View view) {
        ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mParams.leftMargin = mParams.rightMargin = dpToPx(16);
        view.setLayoutParams(mParams);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final ConsentView consentItem = items.get(position - HEADER_COUNT);
            ((PermissionViewHolder) holder).setDefinition(consentItem);
        } else if (getItemViewType(position) == TYPE_HEADER) {

        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof PermissionViewHolder) {
            ((PermissionViewHolder) holder).toggle.setOnCheckedChangeListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + HEADER_COUNT;
    }

    void onGetConsentRetrieved(@NonNull final List<ConsentView> consentViews) {
        items.clear();
        items.addAll(consentViews);
        notifyItemRangeChanged(HEADER_COUNT, consentViews.size() + HEADER_COUNT);
    }

    void onGetConsentFailed(ConsentNetworkError error) {
        for (ConsentView consentView : items) {
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getCatkErrorCode() != CatkConstants.CONSENT_ERROR_NO_CONNECTION);
        }
        notifyItemRangeChanged(HEADER_COUNT, items.size() + HEADER_COUNT);
    }

    void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error) {
        int position = getIndexOfViewWithDefinition(definition);
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position);
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getCatkErrorCode() != CatkConstants.CONSENT_ERROR_NO_CONNECTION);
            notifyItemChanged(position + HEADER_COUNT);
        }
    }

    void onCreateConsentSuccess(RequiredConsent consent) {
        int position = getIndexOfViewWithDefinition(consent.getDefinition());
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position);
            consentView.setError(false);
            consentView.setIsLoading(false);
            consentView.storeConsent(consent);
            notifyItemChanged(position + HEADER_COUNT);
        }
    }

    private int getIndexOfViewWithDefinition(ConsentDefinition definition) {
        for (int index = 0; index < items.size(); index++) {
            final ConsentView consentView = items.get(index);
            if (consentView.getDefinition().equals(definition)) {
                return index;
            }
        }
        return NOT_FOUND;
    }

    @NonNull
    List<ConsentView> getConsentViews() {
        return new ArrayList<>(items);
    }

    static class PermissionViewHolder extends RecyclerView.ViewHolder {

        private Switch toggle;
        private Label label;
        private Label help;
        private Label error;
        private ProgressBar progress;
        @Nullable
        private ConsentToggleListener consentToggleListener;
        @NonNull
        private HelpClickListener helpClickListener;

        PermissionViewHolder(@NonNull View itemView, @NonNull HelpClickListener helpClickListener, @Nullable ConsentToggleListener consentToggleListener) {
            super(itemView);
            this.toggle = itemView.findViewById(R.id.toggleicon);
            this.label = itemView.findViewById(R.id.consentText);
            this.help = itemView.findViewById(R.id.consentHelp);
            this.error = itemView.findViewById(R.id.consentError);
            this.progress = itemView.findViewById(R.id.progressBar);
            this.consentToggleListener = consentToggleListener;
            this.helpClickListener = helpClickListener;
            this.help.setPaintFlags(this.help.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        void setDefinition(final ConsentView consentView) {
            // Update UI here
            label.setText(consentView.getConsentText());
            progress.setVisibility(consentView.isLoading() ? View.VISIBLE : View.INVISIBLE);
            toggle.setVisibility(consentView.isLoading() ? View.INVISIBLE : View.VISIBLE);
            error.setVisibility(consentView.isError() ? View.VISIBLE : View.INVISIBLE);
            toggle.animate().alpha(consentView.isError() ? 0.5f : 1.0f).start();
            toggle.setEnabled(consentView.isEnabled());
            toggle.setChecked(consentView.isChecked());
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (consentToggleListener != null) {
                        setLoading(consentView);
                        consentToggleListener.onToggledConsent(consentView.getDefinition(), b);
                    }
                }
            });
            linkifyPrivacyPolicy(help, new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    helpClickListener.onHelpClicked(consentView.getHelpText());
                }
            });
        }



        private void setLoading(ConsentView consentView) {
            consentView.setIsLoading(true);
            consentView.setError(false);
            progress.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        }
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private static void linkifyPrivacyPolicy(TextView pTvPrivacyPolicy, ClickableSpan span) {
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


    class Header extends RecyclerView.ViewHolder {

        public Header(View itemView) {
            super(itemView);
        }
    }
}
