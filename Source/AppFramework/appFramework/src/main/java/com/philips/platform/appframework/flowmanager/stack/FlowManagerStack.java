package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;

import java.util.ArrayList;

public class FlowManagerStack extends ArrayList<BaseState> {

    //TODO - need to revise again
    public void push(BaseState baseState) {
        if (!contains(baseState))
            add(baseState);
    }

    public BaseState pop() throws NoStateException {
        if (size() > 1) {
            remove(size() - 1);
            return get(size() - 1);
        } else
            throw new NoStateException();
    }

    public BaseState pop(BaseState state) throws NoStateException {
        int index = indexOf(state);
        int remove = size() - (index + 1);
        BaseState nextState = null;
        for (int i = 0; i < remove; i++) {
            nextState = pop();
        }
        return nextState;
    }
}
