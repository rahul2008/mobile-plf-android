package com.philips.platform.uid.view.widget;

/**
 * The interface Uid expander delegate.
 */
public interface UIDExpanderDelegate {

    /**
     * Expander panel will expand.
     * This method will call when the expander is about to expand
     * @since 1805.0.0
     */

    public void expanderPanelWillExpand();

    /**
     * Expander panel did expand.
     * This method will call when the expander finished expanding
     * @since 1805.0.0
     */
    public void expanderPanelDidExpand();

    /**
     * Expander panel will collapse.
     * This method will call when the expander is about to collapse
     * @since 1805.0.0
     */
    public void expanderPanelWillCollapse();

    /**
     * Expander panel did collapse.
     * This method will call when the expander finished collapsing
     * @since 1805.0.0
     */
    public void expanderPanelDidCollapse();
}
