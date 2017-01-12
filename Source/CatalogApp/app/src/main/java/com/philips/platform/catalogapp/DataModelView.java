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

public class DataModelView extends BaseObservable {
    @Bindable
    public ObservableArrayList<Datamodel> datamodels;

    public DataModelView() {
        this.datamodels = new ObservableArrayList<>();
    }

    public void addUser(int icon, int title, final Context context) {
        this.datamodels.add(new Datamodel(icon, title, context));
    }
}
