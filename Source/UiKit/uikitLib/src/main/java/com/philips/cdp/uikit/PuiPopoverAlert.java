package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
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

    private static int viewId = 10050001;
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
        progressBar.setId(viewId++);
        rightIconImageView = (ImageView) findViewById(R.id.uikit_popover_close_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.popover_alert);
        Drawable leftIcon = a.getDrawable(R.styleable.popover_alert_popover_left_icon);
        Drawable rightIcon = a.getDrawable(R.styleable.popover_alert_popover_right_icon);
        String titleString = (String) a.getText(R.styleable.popover_alert_popover_title_text);
        float alpha = a.getFloat(R.styleable.popover_alert_popover_opacity,0.8f);
        a.recycle();

        setAlpha(alpha);
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

        //Set via code to avoid the tailing issue of background drawable
        Drawable d = ResourcesCompat.getDrawable(context.getResources(), R.drawable
                .uikit_popover_progress_bar_drawable, context.getTheme());
        progressBar.setProgressDrawable(d);

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

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setVisibility(savedState.visible);
        progressBar.setProgress(savedState.progress);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.progress = progressBar.getProgress();
        savedState.visible = getVisibility();
        return savedState;
    }

    static class SavedState extends BaseSavedState {

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(final Parcel source) {
                        return new SavedState(source);
                    }

                    @Override
                    public SavedState[] newArray(final int size) {
                        return new SavedState[size];
                    }
                };

        int progress;
        int visible;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.progress = in.readInt();
            this.visible = in.readInt();
        }

        @Override
        public void writeToParcel(final Parcel out, final int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.progress);
            out.writeInt(this.visible);
        }
    }
}
