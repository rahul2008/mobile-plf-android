/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.uid;

public class Control {
    String component;

    String[] state;

    String context;

    String[] parent;

    Property property;

    public Control(
            final String component, final String[] state,
            final String context, final String[] parent,
            final Property property) {
        this.component = component;
        this.state = state;
        this.context = context;
        this.parent = parent;
        this.property = property;
    }

    public String getAttributeName(String parent) {
        final String states = getStates();
        String attrName = String.format("%s_%s_%s_%s_%s", component, context == null ? "" : context, states, property.getItem(), property.getType());
        if (parent != null) {
            attrName = String.format("%s_%s", parent, attrName);
        }
        return attrName.replace('-', '_').toLowerCase();
    }

    public String getStates() {

        StringBuffer appenededState = new StringBuffer();
        if (state.length > 0) {
            for (String curState : state) {
                appenededState.append("_");
                appenededState.append(curState);
            }
        }
        return appenededState.toString();
    }

    @Override
    public String toString() {
        return "Control [component = " + component + ", state = " + state + ", context = " + context + ", parent = " + parent + ", property = " + property + "ATT " + getAttributeName(parent.length > 0 ? parent[0] : null) + "]";
    }
}
