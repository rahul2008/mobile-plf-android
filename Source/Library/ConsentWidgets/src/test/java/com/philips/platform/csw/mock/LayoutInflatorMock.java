package com.philips.platform.csw.mock;

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
