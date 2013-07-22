package com.philips.cl.di.dev.pa.interfaces;

/**
 * Callback interface to interact with the HSV.
 */
public interface SizeCallback {
    /**
     * Used to allow clients to measure Views before re-adding them.
     */
    public void onGlobalLayout();

    /**
     * Used by clients to specify the View dimensions.
     * 
     * @param idx
     *            Index of the View.
     * @param w
     *            Width of the parent View.
     * @param h
     *            Height of the parent View.
     * @param dims
     *            dims[0] should be set to View width. dims[1] should be set to View height.
     */
    public void getViewSize(int idx, int w, int h, int[] dims);
}
