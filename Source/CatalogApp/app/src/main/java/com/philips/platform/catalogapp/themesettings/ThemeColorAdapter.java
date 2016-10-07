/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.ThemeColorHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeColorAdapter extends RecyclerView.Adapter<ThemeColorAdapter.ViewHolder> {
    List<ColorModel> colorRangeList;
    private ThemeChangedListener themeChangedListener;
    private int colorPickerwidth;
    private int selectedPosition = 0;

    public ThemeColorAdapter(@NonNull final List<ColorModel> colorRangeList, @NonNull final ThemeChangedListener themeChangedListener, final int colorPickerwidth) {
        this.colorRangeList = colorRangeList;
        this.themeChangedListener = themeChangedListener;
        this.colorPickerwidth = colorPickerwidth;
    }

    @Override
    public ThemeColorAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, @NonNull final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_selector_list_item, parent, false);
        view.setMinimumWidth(colorPickerwidth);
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = colorPickerwidth;
        view.setLayoutParams(layoutParams);
        ThemeColorAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemeColorAdapter.ViewHolder holder, final int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ColorModel colorModel = colorRangeList.get(position);
        holder.colorRangeTittleLabel.setText(colorModel.getTitle());
        ThemeColorHelper colorListHelper = new ThemeColorHelper();
        final Context context = holder.itemView.getContext();
        if (colorModel.getName() != null && !colorModel.getName().isEmpty()) {
            final int startColors = colorListHelper.getColorResourceId(context.getResources(), colorModel.getName(), "50", context.getPackageName());
            final int endColors = colorListHelper.getColorResourceId(context.getResources(), colorModel.getName(), "35", context.getPackageName());
            holder.itemView.setBackground(getItemviewBackground(ContextCompat.getColor(context, startColors), ContextCompat.getColor(context, endColors), context));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, colorModel.getColor()));
        }
        holder.colorRangeSelectedCheckBox.setVisibility(adapterPosition == selectedPosition ? View.VISIBLE : View.GONE);

        holder.colorRangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
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

    private GradientDrawable getItemviewBackground(final int startColor, final int endColor, final Context context) {
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{startColor, endColor});
        return gd;
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
