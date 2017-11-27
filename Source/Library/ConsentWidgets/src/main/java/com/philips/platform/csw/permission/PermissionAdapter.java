/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder> {

    private static final int NOT_FOUND = -1;
    @NonNull
    private final List<ConsentView> items;
    @NonNull
    private final HelpClickListener helpClickListener;

    @Nullable
    private ConsentToggleListener consentToggleListener;

    @Inject
    PermissionAdapter(@NonNull final List<ConsentView> definitions, @NonNull HelpClickListener helpClickListener) {
        this.items = new ArrayList<>(definitions);
        this.helpClickListener = helpClickListener;
    }

    void setConsentToggleListener(@Nullable ConsentToggleListener consentToggleListener) {
        this.consentToggleListener = consentToggleListener;
    }

    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_consent, parent, false);
        return new PermissionViewHolder(view, helpClickListener, consentToggleListener);
    }

    @Override
    public void onBindViewHolder(PermissionViewHolder holder, int position) {
        final ConsentView consentItem = items.get(position);
        holder.setDefinition(consentItem);
    }

    @Override
    public void onViewRecycled(PermissionViewHolder holder) {
        super.onViewRecycled(holder);
        holder.toggle.setOnCheckedChangeListener(null);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void onGetConsentRetrieved(@NonNull final List<ConsentView> consentViews) {
        items.clear();
        items.addAll(consentViews);
        notifyItemRangeChanged(0, consentViews.size());
    }

    void onGetConsentFailed(ConsentNetworkError error) {
        for (ConsentView consentView : items) {
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getCatkErrorCode() != CatkConstants.CONSENT_ERROR_NO_CONNECTION);
        }
        notifyItemRangeChanged(0, items.size());
    }

    void onCreateConsentFailed(ConsentDefinition definition, ConsentNetworkError error) {
        int position = getIndexOfViewWithDefinition(definition);
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position);
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getCatkErrorCode() != CatkConstants.CONSENT_ERROR_NO_CONNECTION);
            notifyItemChanged(position);
        }
    }

    void onCreateConsentSuccess(RequiredConsent consent) {
        int position = getIndexOfViewWithDefinition(consent.getDefinition());
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position);
            consentView.setError(false);
            consentView.setIsLoading(false);
            consentView.storeConsent(consent);
            notifyItemChanged(position);
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
            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
}
