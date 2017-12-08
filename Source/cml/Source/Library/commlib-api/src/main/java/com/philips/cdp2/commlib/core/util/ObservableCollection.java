/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

public interface ObservableCollection<T> {
    void addModificationListener(T forObject, ModificationListener<T> listener);

    void removeModificationListener(T forObject, ModificationListener<T> listener);

    interface ModificationListener<T> {
        void onRemoved(T object);

        void onAdded(T object);
    }
}
