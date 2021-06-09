package com.example.android.taskonewithactivity.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {

    public ItemContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.android.taskonewithactivity";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String TABLE_NAME = "item";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_FIRST_NAME = "firstName";

        public static final String COLUMN_LAST_NAME = "lastName";

        public static final String COLUMN_EMAIL_ID = "emailId";

        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_WEIGHT = "weight";

        public static final String COLUMN_HEIGHT = "height";

        public static final String COLUMN_PHONE = "phone";

        public static final String COLUMN_DATE = "date";
    }
}
