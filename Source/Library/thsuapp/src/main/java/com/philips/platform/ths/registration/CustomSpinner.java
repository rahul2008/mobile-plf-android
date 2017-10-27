/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner{

        OnItemSelectedListener listener;

        public CustomSpinner(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setSelection(int position) {
            super.setSelection(position);
            if (listener != null)
                listener.onItemSelected(null, null, position, 0);
        }

        public void setOnItemSelectedEvenIfUnchangedListener(
                OnItemSelectedListener listener) {
            this.listener = listener;
        }

}
