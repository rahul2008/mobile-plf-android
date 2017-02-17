/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;

public class DataHolderView extends BaseObservable {
    @Bindable
    public ObservableArrayList<DataHolder> dataHolders;

    public DataHolderView() {
        this.dataHolders = new ObservableArrayList<>();
    }

    public void addIconItem(int icon, int title, final Context context) {
        this.dataHolders.add(new DataHolder(icon, title, title , context));
    }

    public void addTwoLineItem(int title, int description, final Context context) {
        this.dataHolders.add(new DataHolder(0, title, description , context));
    }
}
