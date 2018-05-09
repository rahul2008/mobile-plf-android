/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class BasePermissionViewHolder extends RecyclerView.ViewHolder {
    public BasePermissionViewHolder(View itemView, int parentWidth) {
        super(itemView);
        applyParams(itemView, parentWidth);
    }

    public abstract void onViewRecycled();

    private void applyParams(View view, int width) {
        if (width < dpToPx(648)) {
            applyDefaultMargin(view);
        } else {
            setMaxWidth(view);
        }
    }

    private void setMaxWidth(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = dpToPx(648);
        view.setLayoutParams(params);
    }

    private void applyDefaultMargin(View view) {
        ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mParams.leftMargin = mParams.rightMargin = dpToPx(16);
        view.setLayoutParams(mParams);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
