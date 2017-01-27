/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.philips.platform.uid.R;

public class RecyclerView extends RelativeLayout {
    public RecyclerView(final Context context) {
        this(context, null);
    }

    public RecyclerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(View.inflate(getContext(), R.layout.uid_recyclerview, null));
    }

    public android.support.v7.widget.RecyclerView getRecyclerView() {
        return (android.support.v7.widget.RecyclerView) findViewById(R.id.uid_recyclerview_recyclerview);
    }

    public void setHeaderEnabled(boolean enabled) {
        findViewById(R.id.uid_recyclerview_title).setVisibility(enabled ? View.VISIBLE : View.GONE);
    }
}
