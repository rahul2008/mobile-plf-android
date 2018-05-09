package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;

import java.util.ArrayList;

/**
 * The class handles the stack of the states.
 * @since 1.1.0
 */

public class FlowManagerStack extends ArrayList<BaseState> {

    private static final long serialVersionUID = -4411367948251552512L;

    /**
     * Push the state.
     * @param baseState the base state
     * @since 1.1.0
     */
    public void push(BaseState baseState) {
        add(baseState);
    }

    /**
     * Pop the state.
     * @return returns the base state
     * @since 1.1.0
     */
    public BaseState pop() {
        if (size() > 1) {
            remove(size() - 1);
            return get(size() - 1);
        } else if (size() == 1) {
            remove(0);
        }
        return null;
    }

    /**
     * Pop the state.
     * @param state Pass current state
     * @return State which is required to update current state
     * @since 1.1.0
     */
    public BaseState pop(BaseState state) {
        int index = lastIndexOf(state);
        int remove = size() - (index + 1);
        BaseState nextState = null;
        for (int i = 0; i < remove; i++) {
            nextState = pop();
        }
        return nextState;
    }
}
