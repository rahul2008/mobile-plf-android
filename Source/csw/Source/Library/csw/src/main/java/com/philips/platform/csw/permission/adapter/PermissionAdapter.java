/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.csw.R;
import com.philips.platform.csw.permission.ConsentView;
import com.philips.platform.csw.permission.HelpClickListener;
import com.philips.platform.csw.permission.PermissionContract;
import com.philips.platform.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PermissionAdapter extends RecyclerView.Adapter<BasePermissionViewHolder> {

    private static final int NOT_FOUND = -1;
    private static final int HEADER_COUNT = 1;
    private static final int VERSION_MISMATCH_ERROR = 1252;


    @NonNull
    private final List<ConsentView> items;
    @NonNull
    private final HelpClickListener helpClickListener;

    @Nullable
    private PermissionContract.Presenter presenter;

    @Nullable
    private LinkSpanClickListener privacyNoticeClickListener;

    @Inject
    public PermissionAdapter(@NonNull final List<ConsentView> definitions, @NonNull HelpClickListener helpClickListener) {
        this.items = new ArrayList<>(definitions);
        this.helpClickListener = helpClickListener;
    }

    public void setPresenter(@Nullable PermissionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void setPrivacyNoticeClickListener(@Nullable LinkSpanClickListener privacyNoticeClickListener) {
        this.privacyNoticeClickListener = privacyNoticeClickListener;
    }

    @Override
    public BasePermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_row, parent, false);
            return new PermissionViewHolder(view, helpClickListener, presenter);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.csw_permission_list_header, parent, false);
            return new PermissionHeaderViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(BasePermissionViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final ConsentView consentItem = items.get(position - HEADER_COUNT);
            ((PermissionViewHolder) holder).setDefinition(consentItem);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            ((PermissionHeaderViewHolder) holder).setPrivacyURL(privacyNoticeClickListener);
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
            consentView.setError(false);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getErrorCode() != ConsentError.CONSENT_ERROR_NO_CONNECTION);
        }
        notifyItemRangeChanged(HEADER_COUNT, items.size() + HEADER_COUNT);
    }

    public void onCreateConsentFailed(final int position, ConsentError error) {
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position - 1);
            consentView.setError(true);
            consentView.setIsLoading(false);
            consentView.setOnline(error.getErrorCode() != ConsentError.CONSENT_ERROR_NO_CONNECTION);
            if (error.getErrorCode() == VERSION_MISMATCH_ERROR) {
                consentView.setEnabledFlag(false);
            }
            notifyItemChanged(position);
        }
    }

    public void onCreateConsentSuccess(final int position, boolean status) {
        if (position != NOT_FOUND) {
            ConsentView consentView = items.get(position - 1);
            consentView.storeConsentDefnitionStatus(getConsentDefinitionStatus(consentView.getDefinition(), status, consentView.getTimestamp()));
            notifyItemChanged(position);
        }
    }

    private ConsentDefinitionStatus getConsentDefinitionStatus(ConsentDefinition definition, boolean status, Date timestamp) {
        ConsentStates consentStatus = status ? ConsentStates.active : ConsentStates.rejected;
        return new ConsentDefinitionStatus(consentStatus, ConsentVersionStates.InSync, definition, timestamp);
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
