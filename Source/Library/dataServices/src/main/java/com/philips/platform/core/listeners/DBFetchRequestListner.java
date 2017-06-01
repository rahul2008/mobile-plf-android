/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

import java.util.List;

/**
 * Interface Callback for notifying Fetch success or failure
 * @param <T> The Sync DataType (Ex: Moment)
 */
public interface DBFetchRequestListner<T> {
    /**
     * Used for Notifying the Propositions in case of Fetch Success.
     * @param data The fetch dataset returned by the query result
     */
    void onFetchSuccess(List<? extends T> data);

    /**
     * Used for Notifying the Propositions in case of Fetch Failure.
     * @param exception The Exception occurred while fetching from Data-Base
     */
    void onFetchFailure(Exception exception);
}
