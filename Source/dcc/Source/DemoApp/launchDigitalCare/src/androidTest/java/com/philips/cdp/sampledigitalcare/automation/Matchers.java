package com.philips.cdp.sampledigitalcare.automation;

/**
 * Created by arbin on 28/04/2017.
 */

public class Matchers {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
