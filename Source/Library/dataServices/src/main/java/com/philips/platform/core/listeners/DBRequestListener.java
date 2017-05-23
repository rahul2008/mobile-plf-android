/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.listeners;

import java.util.List;

/**
 * Interface Callback for notifying DB-Request success or failure
 * @param <T> The Sync DataType (Ex: Moment)
 */
public interface DBRequestListener<T> {

    /**
     * Used for Notifying the Propositions in case of DBRequest Success.
     * @param data The data-set returned by the query result
     */
    void onSuccess(List<? extends T> data);

    /**
     * Used for Notifying the Propositions in case of DBRequest Failure.
     * @param exception The exception occurred while performing DB operations.
     */
    void onFailure(Exception exception);
}
