package com.philips.platform.csw.permission.uielement;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.philips.platform.uid.view.widget.Switch;

public class SilenceableSwitch extends Switch {
    private boolean willNotify = true;

    public SilenceableSwitch(Context context) {
        super(context);
    }

    public SilenceableSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SilenceableSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChecked(boolean checked, boolean notify) {
        willNotify = notify;
        setChecked(checked);
        willNotify = true;
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable final OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (listener == null) {
                    return;
                }

                if (willNotify) {
                    listener.onCheckedChanged(compoundButton, checked);
                }
            }
        });
    }
}
