package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.philips.platform.uid.R;


public class ValidationEditText extends EditText {

    private static final int[] ERROR_STATE_SET = {R.attr.state_error};
    private boolean showingError;

    public ValidationEditText(@NonNull Context context) {
        super(context);
    }

    public ValidationEditText(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidationEditText(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if(showingError) {
            mergeDrawableStates(drawableState, ERROR_STATE_SET);
        }
        return drawableState;
    }

    public void showError(boolean showingError) {
        if(this.showingError != showingError) {
            this.showingError = showingError;
            refreshDrawableState();
        }
    }
}