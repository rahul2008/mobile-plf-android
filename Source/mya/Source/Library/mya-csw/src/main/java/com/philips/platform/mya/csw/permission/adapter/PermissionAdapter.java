/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.permission.ConsentToggleListener;
import com.philips.platform.mya.csw.permission.ConsentView;
import com.philips.platform.mya.csw.permission.HelpClickListener;
import com.philips.platform.mya.csw.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PermissionAdapter extends RecyclerView.Adapter<BasePermissionViewHolder> {

    private static final int NOT_FOUND = -1;
    private static final int HEADER_COUNT = 1;

    @NonNull
    private final List<ConsentView> items;
    @NonNull
    private final HelpClickListener helpClickListener;

    @Nullable
    private ConsentToggleListener consentToggleListener;

    @Inject
    public PermissionAdapter(@NonNull final List<ConsentView> definitions, @NonNull HelpClickListener helpClickListener) {
        this.items = new ArrayList<>(definitions);
        this.helpClickListener = helpClickListener;
    }

    public void setConsentToggleListener(@Nullable ConsentToggleListener consentToggleListener) {
        this.consentToggleListener = consentToggleListener;
    }

    @Override
    public BasePermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_row, parent, false);
            return new PermissionViewHolder(view, parent.getWidth(), helpClickListener, consentToggleListener);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_header, parent, false);
            return new PermissionHeaderViewHolder(view, parent.getWidth());
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(BasePermissionViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final ConsentView consentItem = items.get(position - HEADER_COUNT);
            ((PermissionViewHolder) holder).setDefinition(consentItem);
        }
    }

    @Override
    public void onViewRecycled(BasePermissionViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public int getItemCount() {
        return items.size() + HEADER_COUNT;
    }

    public void onGetConsentRetrieved(@NonNull final List<ConsentView> consentViews) {
        items.clear();
        items.addAll(consentViews);
        notifyItemRangeChanged(HEADER_COUNT, consentViews.size() + HEADER_COUNT);
    }

    public void onGetConsentFailed(ConsentError error) {
        for (ConsentView consentView : items) {
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getErrorCode() != ConsentError.CONSENT_ERROR_NO_CONNECTION);
        }
        notifyItemRangeChanged(HEADER_COUNT, items.size() + HEADER_COUNT);
    }

    public void onCreateConsentFailed(ConsentDefinition definition, ConsentError error) {
        int position = getIndexOfViewWithDefinition(definition);
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position);
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getErrorCode() != ConsentError.CONSENT_ERROR_NO_CONNECTION);
            notifyItemChanged(position + HEADER_COUNT);
        }
    }

    public void onCreateConsentSuccess(Consent consent) {
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
    public List<ConsentView> getConsentViews() {
        return new ArrayList<>(items);
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
}
