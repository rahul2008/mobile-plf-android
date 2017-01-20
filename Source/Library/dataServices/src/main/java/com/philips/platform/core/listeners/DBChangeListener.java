package com.philips.platform.core.listeners;

/**
 * Created by sangamesh on 18/01/17.
 */

public interface DBChangeListener {

    void dBChangeSuccess();
    void dBChangeFailed(Exception e);
}
