package com.philips.platform.core.listeners;

import java.util.List;

/**
 * Created by 310218660 on 2/21/2017.
 */

public interface DBFetchRequestListner<T> {
    void onFetchSuccess(List<? extends T> data);
    void onFetchFailure(Exception exception);
}
