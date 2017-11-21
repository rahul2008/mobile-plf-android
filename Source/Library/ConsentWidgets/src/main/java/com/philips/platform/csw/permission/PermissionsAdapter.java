package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Entreco on 17/11/2017.
 */

class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionViewHolder> {

    private final List<ConsentView> items;
    private final CreateConsentInteractor createConsentInteractor;

    PermissionsAdapter(List<ConsentView> definitions, CreateConsentInteractor createConsentInteractor) {
        this.items = new ArrayList<>(definitions);
        this.createConsentInteractor = createConsentInteractor;
    }

    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_consent, parent, false);
        return new PermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PermissionViewHolder holder, int position) {
        final ConsentView consentItem = items.get(position);
        holder.setCreateInteractor(createConsentInteractor);
        holder.setDefinition(consentItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void onConsentRetrieved(@NonNull List<ConsentView> consentViews) {
        items.clear();
        items.addAll(consentViews);
        notifyItemRangeChanged(0, consentViews.size());
    }

    static class PermissionViewHolder extends RecyclerView.ViewHolder{

        private Switch toggle;
        private Label label;
        private Label help;
        private ProgressBar progress;
        @Nullable
        private CreateConsentInteractor createConsentInteractor;

        PermissionViewHolder(View itemView) {
            super(itemView);
            this.toggle = itemView.findViewById(R.id.toggleicon);
            this.label = itemView.findViewById(R.id.consentText);
            this.help = itemView.findViewById(R.id.consentHelp);
            this.progress = itemView.findViewById(R.id.progressBar);
        }

        void setCreateInteractor(CreateConsentInteractor createConsentInteractor){
            this.createConsentInteractor = createConsentInteractor;
        }

        void setDefinition(final ConsentView definition) {
            // Update UI here
            label.setText(definition.getConsentText());
            progress.setVisibility(definition.isLoading() ? View.VISIBLE : View.GONE);
            toggle.setEnabled(definition.isEnabled());
            toggle.setChecked(definition.isChecked());
            toggle.setVisibility(definition.isLoading() ? View.INVISIBLE : View.VISIBLE);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (createConsentInteractor != null) {
                        createConsentInteractor.createConsentStatus(definition, b);
                    }
                }
            });
        }
    }
}
