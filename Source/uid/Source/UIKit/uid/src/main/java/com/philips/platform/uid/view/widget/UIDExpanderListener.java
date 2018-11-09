/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

/**
 *  UIDExpanderListener is a protocol for DLS Expander.
 *  This protocol is responsible for giving the callbacks in DLS Expander.
 *
 *  - Since: 1805.0.0
 */

public interface UIDExpanderListener {

    /**
     * Expander panel did expand.
     * This method will call when the expander finished expanding
     * @since 1805.0.0
     */
    public void expanderPanelExpanded(Expander expander);

    /**
     * Expander panel did collapse.
     * This method will call when the expander finished collapsing
     * @since 1805.0.0
     */
    public void expanderPanelCollapsed(Expander expander);
}
