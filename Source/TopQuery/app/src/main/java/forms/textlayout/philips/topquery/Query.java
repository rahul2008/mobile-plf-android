package forms.textlayout.philips.topquery;

import android.support.annotation.NonNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface Query {
    @NonNull
    String topQuery(String query, int maxResults);
}
