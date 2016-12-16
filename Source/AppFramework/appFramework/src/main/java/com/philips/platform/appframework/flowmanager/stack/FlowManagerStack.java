package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;

import java.util.ArrayList;
import java.util.List;

public class FlowManagerStack {

    private List<BaseState> stack;

    public FlowManagerStack() {
        stack = new ArrayList<>();
    }

    public void push(BaseState baseState) {
        if (!stack.contains(baseState))
            stack.add(baseState);
    }

    public BaseState pop() throws NoStateException {
        int size = stack.size();
        if (size > 1) {
            stack.remove(size - 1);
            return stack.get(size - 1);
        } else
            throw new NoStateException();
    }

    public BaseState pop(BaseState state) throws NoStateException {
        int index = stack.indexOf(state);
        int remove = stack.size() - (index + 1);
        BaseState nextState = null;
        for (int i = 0; i < remove; i++) {
            nextState = pop();
        }
        return nextState;
    }

    public int size() {
        return stack.size();
    }

    public void clear() {
        stack.clear();
    }
}
