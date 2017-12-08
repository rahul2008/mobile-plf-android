/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import com.philips.platform.uid.R;


public class ValidationEditText extends EditText {

    private static final int[] ERROR_STATE_SET = {R.attr.uid_state_error};
    private boolean isInErrorState;

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
        if(isInErrorState) {
            mergeDrawableStates(drawableState, ERROR_STATE_SET);
        }
        return drawableState;
    }

    /**
     * Set/remove error state in {@link EditText}
     * @param isError boolean to set state
     */
    public void setError(boolean isError) {
        if(this.isInErrorState != isError) {
            this.isInErrorState = isError;
            refreshDrawableState();
        }
    }
}
