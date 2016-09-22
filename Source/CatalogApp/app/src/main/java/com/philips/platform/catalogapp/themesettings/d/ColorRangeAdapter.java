package com.philips.platform.catalogapp.themesettings;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.themesettings.d.model.ColorRangeModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ColorRangeAdapter extends RecyclerView.Adapter<ColorRangeAdapter.ViewHolder> {
    private List<ColorRangeModel> colorRangeList;
    private com.philips.platform.catalogapp.themesettings.ColorRangChangedListener colorRangChangedListener;
    private int selectedPosition = 0;

    public ColorRangeAdapter(@NonNull final List<ColorRangeModel> colorRangeList, @NonNull com.philips.platform.catalogapp.themesettings.ColorRangChangedListener colorRangChangedListener) {
        this.colorRangeList = colorRangeList;
        this.colorRangChangedListener = colorRangChangedListener;
    }

    @Override
    public ColorRangeAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, @NonNull final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theme_selector_list_item, parent, false);
        ColorRangeAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ColorRangeAdapter.ViewHolder holder, @NonNull final int position) {
        final ColorRangeModel colorRangeModel = colorRangeList.get(position);
        holder.colorRangeTittleLabel.setText(colorRangeModel.getTitle());
        holder.colorRangeSelectedCheckBox.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

        holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(colorRangeModel.getColor()));
        holder.colorRangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int adapterPosition = holder.getAdapterPosition();
                selectedPosition = adapterPosition;
                notifyDataSetChanged();
                if (colorRangChangedListener != null) {
                    final ColorRangeModel colorRangeModel = colorRangeList.get(selectedPosition);

                    colorRangChangedListener.onColorRangeChanged(colorRangeModel.getName());
                }
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

        public ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
