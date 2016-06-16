package com.philips.cdp.appframework.statemachine;

import java.util.List;

/**
 * Created by 310240027 on 6/15/2016.
 */
public class StateImpl extends StateBase {
    @Override
    void handleState() {

    }

    @Override
    void handleBehaviour() {

    }

    @Override
    void setTrigger() {

    }

    @Override
    void getAction() {

    }

    @Override
    void addTriggerList(TriggerBase triggerBase) {
        triggerBaseList.add(triggerBase);
    }

    @Override
   public List<TriggerBase> getTriggerBaseList() {
        return triggerBaseList;
    }
}
