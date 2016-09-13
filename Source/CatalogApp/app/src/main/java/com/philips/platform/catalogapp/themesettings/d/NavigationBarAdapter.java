package com.philips.platform.catalogapp.themesettings.d;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.themesettings.d.model.NavigationBarModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NavigationBarAdapter extends RecyclerView.Adapter<NavigationBarAdapter.ViewHolder> {
    List<NavigationBarModel> colorRangeList;
    private int selectedPosition = 0;

    public NavigationBarAdapter(final List<NavigationBarModel> colorRangeList) {
        this.colorRangeList = colorRangeList;
    }

    @Override
    public NavigationBarAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theme_selector_list_item, parent, false);
        NavigationBarAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NavigationBarAdapter.ViewHolder holder, final int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final NavigationBarModel NavigationBarModel = colorRangeList.get(position);
        holder.colorRangeTittleLabel.setText(NavigationBarModel.getTitle());
        holder.colorRangeSelectedCheckBox.setVisibility(adapterPosition == selectedPosition ? View.VISIBLE : View.GONE);

        holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(NavigationBarModel.getColor()));
        holder.colorRangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                selectedPosition = adapterPosition;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorRangeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.colorRangeItemContainer)
        View colorRangeContainer;

        @Bind(R.id.colorRangeTittleText)
        public TextView colorRangeTittleLabel;

        @Bind(R.id.colorRangeSelectedCheckbox)
        public ImageView colorRangeSelectedCheckBox;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
