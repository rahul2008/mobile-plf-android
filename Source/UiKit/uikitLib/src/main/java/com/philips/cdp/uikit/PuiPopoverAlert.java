package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PuiPopoverAlert extends RelativeLayout {

    private Context context;

    private TextView titleText;
    private ImageView leftIconImageView;
    private ProgressBar progressBar;
    private ImageView rightIconImageView;


    public PuiPopoverAlert(final Context context) {
        super(context);
    }

    public PuiPopoverAlert(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uikit_popover_alert, this, true);

        titleText = (TextView) findViewById(R.id.uikit_popover_alert_title);
        leftIconImageView = (ImageView) findViewById(R.id.uikit_popover_info_icon);
        progressBar = (ProgressBar) findViewById(R.id.uikit_popover_progress_bar);
        rightIconImageView = (ImageView) findViewById(R.id.uikit_popover_close_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.popover_alert);
        int backgroundGradientStartColor = a.getColor(R.styleable.popover_alert_popover_background_gradient_start_color, getResources().getColor(R.color.uikit_philips_dark_blue));
        int backgroundGradientEndColor = a.getColor(R.styleable.popover_alert_popover_background_gradient_end_color, getResources().getColor(R.color.uikit_philips_bright_blue));
        Drawable leftIcon = a.getDrawable(R.styleable.popover_alert_popover_left_icon);
        Drawable rightIcon = a.getDrawable(R.styleable.popover_alert_popover_right_icon);
        String titleString = (String) a.getText(R.styleable.popover_alert_popover_title_text);

        setBackgroundDrawable(getBarckgroundGradientDrawable(backgroundGradientStartColor, backgroundGradientEndColor));

        titleText.setText(titleString);
        if(leftIcon != null) {
            leftIconImageView.setImageDrawable(leftIcon);
        } else {
            leftIconImageView.setVisibility(View.GONE);
        }

        if(rightIcon != null) {
            rightIconImageView.setImageDrawable(rightIcon);
        } else {
            rightIconImageView.setVisibility(View.GONE);
        }

        rightIconImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                dismiss();
            }
        });

        setVisibility(View.GONE);
    }

    public PuiPopoverAlert(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Drawable getBarckgroundGradientDrawable(int startColor, int endColor) {
        final Resources resources = context.getResources();
        int[] gradient = new int[]{endColor, startColor};

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, gradient);

        drawable.mutate();
        return drawable;
    }

    public void show() {
        if(!(View.VISIBLE == getVisibility())) {
            setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.uikit_popover_fadein);
            startAnimation(fadeIn);
        }
    }

    public void dismiss() {
        if((View.VISIBLE == getVisibility())) {
            setVisibility(View.GONE);
            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.uikit_popover_fadeout);
            startAnimation(fadeOut);
        }
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(final TextView titleText) {
        this.titleText = titleText;
    }

    public ImageView getLeftIcon() {
        return leftIconImageView;
    }

    public void setLeftIcon(final Drawable leftIcon) {
        this.leftIconImageView.setImageDrawable(leftIcon);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ImageView getRightIcon() {
        return rightIconImageView;
    }

    public void setRightIcon(final Drawable rightIcon) {
        this.rightIconImageView.setImageDrawable(rightIcon);
    }
}
