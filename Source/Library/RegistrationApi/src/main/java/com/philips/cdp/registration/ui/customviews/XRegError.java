/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;

public class XRegError extends RelativeLayout {

    private Context mContext;

    private TextView mTvError;

    private int stateToSave;

    private String mErrMsg;

    public XRegError(Context context) {
        super(context);
        mContext = context;
        initUi(R.layout.reg_error_mapping);
    }

    public XRegError(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUi(R.layout.reg_error_mapping);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end
        ss.stateToSave = this.stateToSave;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end
        this.stateToSave = ss.stateToSave;
    }


    private void initUi(int resourceId) {

        /** inflate amount layout */
        LayoutInflater li = LayoutInflater.from(mContext);
        li.inflate(resourceId, this, true);
        mTvError = (XTextView) findViewById(R.id.tv_reg_error_message);
    }

    public void setError(String errorMsg) {
        if (null == errorMsg) {
            return;
        }
        mErrMsg = errorMsg;
        mTvError.setText(errorMsg);
        setVisibility(VISIBLE);
    }

    public String getError() {
       return mErrMsg;
    }

    public void hideError() {
        setVisibility(GONE);
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
