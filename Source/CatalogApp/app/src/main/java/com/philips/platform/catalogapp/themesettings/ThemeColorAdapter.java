/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeColorAdapter extends RecyclerView.Adapter<ThemeColorAdapter.ViewHolder> {
    List<ColorModel> colorRangeList;
    private ThemeChangedListener themeChangedListener;
    private int selectedPosition = 0;

    public ThemeColorAdapter(@NonNull final List<ColorModel> colorRangeList, @NonNull final ThemeChangedListener themeChangedListener) {
        this.colorRangeList = colorRangeList;
        this.themeChangedListener = themeChangedListener;
    }

    @Override
    public ThemeColorAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, @NonNull final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.theme_selector_list_item, parent, false);
        ThemeColorAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemeColorAdapter.ViewHolder holder, final int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ColorModel colorModel = colorRangeList.get(position);
        final int currentTextColor = holder.colorRangeTittleLabel.getCurrentTextColor();
        final Context context = holder.itemView.getContext();
        holder.colorRangeTittleLabel.setText(colorModel.getTitle());
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, colorModel.getColor()));
        holder.colorRangeSelectedCheckBox.setVisibility(adapterPosition == selectedPosition ? View.VISIBLE : View.GONE);

        holder.colorRangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                selectedPosition = adapterPosition;
                notifyDataSetChanged();
                if (themeChangedListener != null) {
                    final ColorModel colorModel = colorRangeList.get(adapterPosition);

                    if (colorModel.getName() != null) {
                        themeChangedListener.onColorRangeChanged(colorModel.getName());
                    }
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
