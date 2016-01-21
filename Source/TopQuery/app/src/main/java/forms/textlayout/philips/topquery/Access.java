package forms.textlayout.philips.topquery;

import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Access implements Query {
    @Override
    @NonNull
    public String topQuery(final String query, final int maxResults) {
        return "SELECT TOP " + maxResults +
        " * FROM (" + query + ")";
    }
}
