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
    public ObservableArrayList<DataModel> dataModels;

    public DataModelView() {
        this.dataModels = new ObservableArrayList<>();
    }

    public void addUser(int icon, int title, final Context context) {
        this.dataModels.add(new DataModel(icon, title, context));
    }
}
