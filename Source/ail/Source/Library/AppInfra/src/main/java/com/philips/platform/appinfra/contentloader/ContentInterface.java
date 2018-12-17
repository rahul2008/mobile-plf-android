/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import java.io.Serializable;
import java.util.List;

/*
 * The Content Interface.
 */
public interface ContentInterface extends Serializable {
    /**
     * The unique id of this content ID
     * @return unique content ID
     * @since 1.1.0
     */
    String getId();

    /**
     * Check whether this article has the given tag set
     * @return true if the given tag identified by its ID is set for this article
     * @since 1.1.0
     */
    boolean hasTag(String tagID);

    /**
     * @return array of Tags set for this article
     * @since 1.1.0
     */
    List<String> getTags();

    /**
     * @return version number of this article
     * @since 1.1.0
     */
    long getVersion();

    /**
     * @return true if the version of this article is more recent than the given version number
     * @since 1.1.0
     */
    boolean isNewer(long version);

    /**
     * Fill the content instance data model members from the given data blob in JSON format
     * @param json input string in JSON format that is to be parsed to this objects data properties
     * @return true if input JSON could be properly parsed, false otherwise
     * @since 1.1.0
     */
    boolean parseInput(String json);
}
