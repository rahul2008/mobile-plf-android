/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy.customtoggle;


import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.philips.platform.ths.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.view.widget.Label;

public class SegmentControl extends LinearLayout {

    private ControlSelectListener controlSelectListener;
    private final ColorStateList colorStateList;
    private int selectedIndex;

    public void setControlSelectListener(ControlSelectListener controlSelectListener) {
        this.controlSelectListener = controlSelectListener;
    }

    interface ControlSelectListener {
        public void onControlSelected(int id, boolean isChanged, View view);
    }

    public SegmentControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        colorStateList = ThemeUtils.buildColorStateList(getContext(), R.color.segment_text_color);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof Label) {
            child.setClickable(true);
            child.setFocusable(true);
            ((Label) child).setTextColor(colorStateList);
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    updateSelection(v);
                }
            });
        }
    }

    private void updateSelection(View v) {
        selectedIndex = indexOfChild(v);
        resetAllChildrenExceptClicked(v);
        boolean isChanged = !v.isSelected();
        v.setSelected(true);
        if (controlSelectListener != null) {
            controlSelectListener.onControlSelected(v.getId(), isChanged, v);
        }
    }

    public void setSelected(int indexChild) {
        int childCount = getChildCount();
        if (indexChild > childCount) {
            return;
        }
        updateSelection(getChildAt(indexChild));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    private void resetAllChildrenExceptClicked(View clickedView) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = getChildAt(index);
            if (view instanceof Label && view != clickedView) {
                view.setSelected(false);
            }
        }
    }
}
