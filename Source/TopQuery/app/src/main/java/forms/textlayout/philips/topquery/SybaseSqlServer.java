package forms.textlayout.philips.topquery;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SybaseSqlServer implements Query{
    public String topQuery(final String query, final int maxResults) {
        String upperCased = query.toUpperCase();
        if (upperCased.indexOf("SELECT ") != -1)
            return upperCased.replaceFirst("SELECT ",
                    "SELECT TOP " + maxResults + " ");
        else
            return query;
    }
}
