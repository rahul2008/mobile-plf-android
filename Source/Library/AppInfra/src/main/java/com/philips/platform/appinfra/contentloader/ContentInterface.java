/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;

import com.philips.platform.appinfra.contentloader.model.Tag;

import java.util.List;

/*
 * Created by 310209604 on 2016-08-10.
 */
public interface ContentInterface {
    /**
     * The unique id of this content ID
     * @return unique content ID
     */
    String getId();

    /**
     * Check whether this article has the given tag set
     * @return true if the given tag identified by its ID is set for this article
     */
    boolean hasTag(String tagID);

    /**
     * @return array of Tags set for this article
     */
    List<Tag> getTags();

    /**
     * @return version number of this article
     */
    long getVersion();

    /**
     * @return true if the version of this article is more recent than the given version number
     */
    boolean isNewer(long version);

    /**
     * Fill the content instance data model members from the given data blob in JSON format
     * @param json input string in JSON format that is to be parsed to this objects data properties
     * @return true if input JSON could be properly parsed, false otherwise
     */
    boolean parseInput(String json);
}
