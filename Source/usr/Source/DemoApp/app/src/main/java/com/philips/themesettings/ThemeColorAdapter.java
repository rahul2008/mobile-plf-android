/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.themesettings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.urdemolibrary.R;

import java.util.List;

public class ThemeColorAdapter extends RecyclerView.Adapter<ThemeColorAdapter.ViewHolder> {
    private List<ColorModel> colorRangeList;
    private ThemeChangedListener themeChangedListener;
    private int colorPickerwidth;
    private int selectedPosition = 0;
    private VectorDrawableCompat drawableCompat;

    public ThemeColorAdapter(@NonNull final List<ColorModel> colorRangeList, @NonNull final ThemeChangedListener themeChangedListener, final int colorPickerwidth) {
        this.colorRangeList = colorRangeList;
        this.themeChangedListener = themeChangedListener;
        this.colorPickerwidth = colorPickerwidth;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @Override
    public ThemeColorAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, @NonNull final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_selector_list_item, parent, false);
        view.setMinimumWidth(colorPickerwidth);
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = colorPickerwidth;
        view.setLayoutParams(layoutParams);
        ThemeColorAdapter.ViewHolder viewHolder = new ViewHolder(view);
        drawableCompat = VectorDrawableCompat.create(view.getContext().getResources(), R.drawable.ic_transparent_done_icon, view.getContext().getTheme());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemeColorAdapter.ViewHolder holder, final int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ColorModel colorModel = colorRangeList.get(position);
        holder.colorRangeTittleLabel.setTextColor(Color.WHITE);
        holder.colorRangeTittleLabel.setText(colorModel.getTitle());
        ThemeColorHelper colorListHelper = new ThemeColorHelper();
        final Context context = holder.itemView.getContext();

        setColorPickerBackground(holder, colorModel, colorListHelper, context);

        setTickMarckColor(holder, adapterPosition, colorModel, context);

        setPickerTextColor(holder, colorModel, context);

        holder.colorRangeSelectedCheckBox.setVisibility(adapterPosition == selectedPosition ? View.VISIBLE : View.GONE);

        holder.colorRangeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                selectedPosition = adapterPosition;
                notifySelection(adapterPosition);
            }
        });
    }

    private void notifySelection(final int adapterPosition) {
        notifyDataSetChanged();
        if (themeChangedListener != null) {
            final ColorModel colorModel = colorRangeList.get(adapterPosition);

            if (colorModel.getName() != null) {
                themeChangedListener.onThemeSettingsChanged(colorModel.getName());
            }
        }
    }

    private void setPickerTextColor(final @NonNull ViewHolder holder, final ColorModel colorModel, final Context context) {
        if (colorModel.getContentColor() == R.color.uidColorWhite) {
            holder.colorRangeTittleLabel.setTextColor(Color.WHITE);
        } else {
            final int colorResourceId75 = getColorResourceId75(colorModel, context);
            holder.colorRangeTittleLabel.setTextColor(getCompatColor(context, colorResourceId75));
        }
    }

    private int getCompatColor(final Context context, final int colorResourceId75) {
        return context.getColor(colorResourceId75);
    }

    private void setTickMarckColor(final @NonNull ViewHolder holder, final int adapterPosition, final ColorModel colorModel, final Context context) {
        if (adapterPosition == selectedPosition) {
            final Drawable mutate = drawableCompat.mutate();
//            final Drawable wrap = DrawableCompat.wrap(mutate);
            if (colorModel.getContentColor() == R.color.uidColorWhite) {
                DrawableCompat.setTint(mutate, Color.WHITE);
            } else {
                final int colorResourceId75 = getColorResourceId75(colorModel, context);
                DrawableCompat.setTint(mutate, getCompatColor(context, colorResourceId75));
            }
            DrawableCompat.setTintMode(mutate, PorterDuff.Mode.SRC_IN);
            holder.colorRangeSelectedCheckBox.setBackground(drawableCompat);
        }
    }

    private int getColorResourceId75(final ColorModel colorModel, final Context context) {
        ThemeColorHelper themeColorHelper = new ThemeColorHelper();
        return themeColorHelper.getColorResourceId(context.getResources(), colorModel.getName(), "75", context.getPackageName());
    }

    private void setColorPickerBackground(final @NonNull ViewHolder holder, final ColorModel colorModel, final ThemeColorHelper colorListHelper, final Context context) {
        if (colorModel.getStartColor() != -1 && colorModel.getEndColor() != -1) {
            int startColors = colorListHelper.getColorResourceId(context.getResources(), colorModel.getName(), String.valueOf(colorModel.getStartColor()), context.getPackageName());
            int endColors = colorListHelper.getColorResourceId(context.getResources(), colorModel.getName(), String.valueOf(colorModel.getEndColor()), context.getPackageName());
            if (colorModel.getEndColor() == 0) {
                startColors = colorListHelper.getColorResourceId(context.getResources(), colorModel.getName(), "5", context.getPackageName());
                endColors = R.color.uidColorWhite;
            }
            holder.itemView.setBackground(getItemviewBackground(getCompatColor(context, startColors), getCompatColor(context, endColors)));
        } else {
            holder.itemView.setBackgroundColor(getCompatColor(context, colorModel.getBackgroundColor()));
        }
    }

    private GradientDrawable getItemviewBackground(final int startColor, final int endColor) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{startColor, endColor});
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return colorRangeList.size();
    }

    public void setColorModels(final List<ColorModel> colorModels) {
        this.colorRangeList = colorModels;
        notifyDataSetChanged();
    }

    public void setSelected(final int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.colorRangeTittleText)
        public TextView colorRangeTittleLabel;

//        @BindView(R.id.colorRangeSelectedCheckbox)
        public ImageView colorRangeSelectedCheckBox;

//        @BindView(R.id.colorRangeItemContainer)
        View colorRangeContainer;

        public ViewHolder(View view) {
            super(view);
            colorRangeTittleLabel = (TextView) view.findViewById(R.id.colorRangeTittleText);
            colorRangeSelectedCheckBox = (ImageView) view.findViewById(R.id.colorRangeSelectedCheckbox);
            colorRangeContainer = view.findViewById(R.id.colorRangeItemContainer);
        }
    }
}
