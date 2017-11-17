package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catk.model.Consent;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Entreco on 17/11/2017.
 */

class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionViewHolder> {

    private final List<ConsentDefinition> items;

    PermissionsAdapter(List<ConsentDefinition> definitions) {
        this.items = new ArrayList<>(definitions);
    }

    @Override
    public PermissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_consent, parent, false);
        return new PermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PermissionViewHolder holder, int position) {
        final ConsentDefinition consentItem = items.get(position);
        holder.setDefinition(consentItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void onConsentsRetrieved(@NonNull List<Consent> consentList) {

//        notifyItemRangeChanged(0, consentList.size());
    }

    static class PermissionViewHolder extends RecyclerView.ViewHolder{

        private Switch toggle;
        private Label label;
        private Label help;

        PermissionViewHolder(View itemView) {
            super(itemView);
            toggle = itemView.findViewById(R.id.toggleicon);
            label = itemView.findViewById(R.id.consentText);
            help = itemView.findViewById(R.id.consentHelp);
        }

        void setDefinition(final ConsentDefinition definition) {
            // Update UI here
            label.setText(definition.getText());
            toggle.setChecked(false);
        }
    }
}
