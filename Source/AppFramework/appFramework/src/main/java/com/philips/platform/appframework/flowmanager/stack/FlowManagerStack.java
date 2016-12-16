package com.philips.platform.appframework.flowmanager.stack;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;

import java.util.ArrayList;
import java.util.List;

public class FlowManagerStack {

    private List<BaseState> baseStateList;

    public FlowManagerStack() {
        baseStateList = new ArrayList<>();
    }

    public void push(BaseState baseState) {
        if (!baseStateList.contains(baseState))
            baseStateList.add(baseState);
    }

    public BaseState pop() throws NoStateException {
        if (baseStateList.size() > 1) {
            baseStateList.remove(baseStateList.size() - 1);
            return baseStateList.get(baseStateList.size() - 1);
        } else
            throw new NoStateException();
    }

    public BaseState pop(BaseState state) throws NoStateException {
        int index = baseStateList.indexOf(state);
        int remove = baseStateList.size() - (index + 1);
        BaseState nextState = null;
        for (int i = 0; i < remove; i++) {
            nextState = pop();
        }
        return nextState;
    }

    protected List<BaseState> getBaseStateList() {
        return baseStateList;
    }

    public void clear() {
        baseStateList.removeAll(baseStateList);
    }
}
