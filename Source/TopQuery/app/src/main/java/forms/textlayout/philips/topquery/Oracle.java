package forms.textlayout.philips.topquery;

import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Oracle implements Query{
    @NonNull
    public String topQuery(final String query, final int maxResults) {
        return  "SELECT * FROM (" + query +
       ") WHERE rownum <= " + maxResults;
    }
}
