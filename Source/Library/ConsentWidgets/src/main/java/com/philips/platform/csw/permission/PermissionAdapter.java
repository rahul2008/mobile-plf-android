/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import java.util.ArrayList;
import java.util.List;

import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.model.RequiredConsent;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder> {

    private static final int NOT_FOUND = -1;
    @NonNull private final List<ConsentView> items;
    @NonNull private final ConsentToggleListener consentToggleListener;

    PermissionAdapter(@NonNull final List<ConsentView> definitions, @NonNull final ConsentToggleListener consentToggleListener) {
        this.items = new ArrayList<>(definitions);
        this.consentToggleListener = consentToggleListener;
    }

    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_consent, parent, false);
        return new PermissionViewHolder(view, consentToggleListener);
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

    void onConsentRetrieved(@NonNull final List<ConsentView> consentViews) {
        items.clear();
        items.addAll(consentViews);
        notifyItemRangeChanged(0, consentViews.size());
    }

    void onConsentGetFailed(int error) {
        for (ConsentView consentView : items) {
            consentView.setError(true);
        }
        notifyItemRangeChanged(0, items.size());
    }

    void onCreateConsentFailed(ConsentDefinition definition, int errorCode) {
        int position = getIndexOfViewWithDefinition(definition);
        if (position != NOT_FOUND) {
            items.get(position).setError(true);
            notifyItemChanged(position);
        }
    }

    void onCreateConsentSuccess(RequiredConsent consent, int code) {
        int position = getIndexOfViewWithDefinition(consent.getDefinition());
        if (position != NOT_FOUND) {
            items.get(position).storeConsent(consent);
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

    static class PermissionViewHolder extends RecyclerView.ViewHolder {

        private Switch toggle;
        private Label label;
        private Label help;
        private Label error;
        private ProgressBar progress;
        @Nullable
        private ConsentToggleListener consentToggleListener;

        PermissionViewHolder(View itemView, ConsentToggleListener consentToggleListener) {
            super(itemView);
            this.toggle = itemView.findViewById(R.id.toggleicon);
            this.label = itemView.findViewById(R.id.consentText);
            this.help = itemView.findViewById(R.id.consentHelp);
            this.error = itemView.findViewById(R.id.consentError);
            this.progress = itemView.findViewById(R.id.progressBar);
            this.consentToggleListener = consentToggleListener;
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
        }

        private void setLoading(ConsentView consentView) {
            consentView.setIsLoading();
            progress.setVisibility( View.VISIBLE );
            error.setVisibility(View.INVISIBLE);
        }
    }
}
