/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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

import com.philips.cdp.uikit.R;

/**
 * Provides information layout which contains progress bar and icons for close and info.<br><br>
 * If used inside relative layout, it must be the last child.<br><br> If used in LinearLayout it must
 * the first child.
 *
 * <p>
 * <H3>Custom Attributes</H3>
 * <b>popover_left_icon:</b> Left icon in the notification.<br>
 * <b>popover_right_icon:</b> icon in the notification.<br>
 * <b>popover_title_text:</b> Title to be displayed<br>
 * </p>
 * <p>
 *     Example:
 *     <pre>
 *             &lt;com.philips.cdp.uikit.customviews.PopoverAlert
 *                  android:id="@+id/popover_alert"
 *                  android:layout_width="match_parent"
 *                  android:layout_height="wrap_content"
 *                  app:popover_left_icon="@drawable/gear"
 *                  app:popover_right_icon="@drawable/apple"
 *                  app:popover_title_text="Uploading settings"/&gt;
 *     </pre>
 * </p>
 *     <H3>UI Appearance</H3>
 *      <img src="../../../../../img/popover_alert.png"
 *      alt="PopOver style." border="0" /></p>
 *
 *
 */
public class PopoverAlert extends RelativeLayout {

    private Context context;

    private TextView titleText;
    private ImageView leftIconImageView;
    private ProgressBar progressBar;
    private ImageView rightIconImageView;


    public PopoverAlert(final Context context) {
        super(context);
    }

    public PopoverAlert(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.uikit_popover_alert, this, true);

        titleText = (TextView) findViewById(R.id.uikit_popover_alert_title);
        leftIconImageView = (ImageView) findViewById(R.id.uikit_popover_info_icon);
        progressBar = (ProgressBar) findViewById(R.id.uikit_popover_progress_bar);
        progressBar.setId(View.generateViewId());
        rightIconImageView = (ImageView) findViewById(R.id.uikit_popover_close_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.popover_alert);
        Drawable leftIcon = a.getDrawable(R.styleable.popover_alert_uikit_popover_left_icon);
        Drawable rightIcon = a.getDrawable(R.styleable.popover_alert_uikit_popover_right_icon);
        String titleString = (String) a.getText(R.styleable.popover_alert_uikit_popover_title_text);
        float alpha = a.getFloat(R.styleable.popover_alert_uikit_popover_opacity,0.8f);
        a.recycle();

        setAlpha(alpha);
        titleText.setText(titleString);
        if (leftIcon != null) {
            leftIconImageView.setImageDrawable(leftIcon);
        } else {
            leftIconImageView.setVisibility(View.GONE);
        }

        if (rightIcon != null) {
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

    public PopoverAlert(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Make the alert visible with animation as applied in uikit_popover_fadein
     */
    public void show() {
        if (!(View.VISIBLE == getVisibility())) {
            setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.uikit_popover_fadein);
            startAnimation(fadeIn);
        }
    }

    /**
     * Hide alert with animation as applied in uikit_popover_fadeout
     */
    public void dismiss() {
        if ((View.VISIBLE == getVisibility())) {
            setVisibility(View.GONE);
            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.uikit_popover_fadeout);
            startAnimation(fadeOut);
        }
    }

    /**
     * Returns TextView holding the title.
     * @return TextView holding the title.
     */
    public TextView getTitleText() {
        return titleText;
    }

    /**
     * Sets the title to the string.
     * @param titleText
     */
    public void setTitleText(final TextView titleText) {
        this.titleText = titleText;
    }

    /**
     * Returns ImageView associated with left Icon
     * @return ImageView associated with left Icon
     */
    public ImageView getLeftIcon() {
        return leftIconImageView;
    }

    /**
     * Sets the left icon
     * @param leftIcon
     */
    public void setLeftIcon(final Drawable leftIcon) {
        this.leftIconImageView.setImageDrawable(leftIcon);
    }

    /**
     * Return the ProgressBar instance used in the alert.
     * @return
     */
    public ProgressBar getProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        return progressBar;
    }

    /**
     * To hide the progressbar
     */

    public void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Returns ImageView associated with right Icon
     * @return ImageView associated with right Icon
     */
    public ImageView getRightIcon() {
        return rightIconImageView;
    }

    /**
     * Sets the right icon
     * @param rightIcon
     */
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

    private static class SavedState extends BaseSavedState {

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
