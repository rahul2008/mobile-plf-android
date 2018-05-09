/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LayoutInflatorMock extends LayoutInflater {
    protected LayoutInflatorMock(Context context) {
        super(context);
    }

    @Override
    public LayoutInflater cloneInContext(Context context) {
        return null;
    }

    @Override
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        usedResource = resource;
        usedViewGroup = root;
        usedAttachToRoot = attachToRoot;
        return inflateViewResult;
    }

    public static LayoutInflatorMock createMock() {
        return new LayoutInflatorMock(new ContextMock());
    }

    public View inflateViewResult;
    public int usedResource;
    public ViewGroup usedViewGroup;
    public boolean usedAttachToRoot;
}
