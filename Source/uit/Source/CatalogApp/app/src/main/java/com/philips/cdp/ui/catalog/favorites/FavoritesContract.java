/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.favorites;

import android.provider.BaseColumns;

public class FavoritesContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FavoritesContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_FAVORITE_ID = "favoriteid";
        public static final String COLUMN_ITEM_NAME = "itemname";
        public static final String COLUMN_IS_FAVORITE = "isfavorite";
    }

}
