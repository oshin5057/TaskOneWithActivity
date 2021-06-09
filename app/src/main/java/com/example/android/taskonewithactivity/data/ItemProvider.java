package com.example.android.taskonewithactivity.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ItemProvider extends ContentProvider {

    public static final String LOG_TAG = ItemProvider.class.getSimpleName();

    private ItemDbHelper mDbHelper;

    private static final int ITEMS = 100;
    private static final int ITEMS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEMS_ID);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                cursor = database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case ITEMS_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor =database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query UNKNOWN Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return insertItem(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertItem(Uri uri, ContentValues values) {

        String firstName = values.getAsString(ItemContract.ItemEntry.COLUMN_FIRST_NAME);
        if (firstName == null){
            throw new IllegalArgumentException("Item require a first name");
        }
        String  lastName = values.getAsString(ItemContract.ItemEntry.COLUMN_LAST_NAME);
        if (lastName == null){
            throw new IllegalArgumentException("Item require a last name");
        }
        String emailId = values.getAsString(ItemContract.ItemEntry.COLUMN_EMAIL_ID);
        if (emailId == null){
            throw new IllegalArgumentException("Item require a email id");
        }
        String address = values.getAsString(ItemContract.ItemEntry.COLUMN_ADDRESS);
        if (address == null){
            throw new IllegalArgumentException("Item require a address");
        }
        String weight = values.getAsString(ItemContract.ItemEntry.COLUMN_WEIGHT);
        if (weight == null){
            throw new IllegalArgumentException("Item require a weight");
        }
        String height = values.getAsString(ItemContract.ItemEntry.COLUMN_HEIGHT);
        if (height == null){
            throw new IllegalArgumentException("Item require a height");
        }
        String phone = values.getAsString(ItemContract.ItemEntry.COLUMN_PHONE);
        if (phone == null){
            throw new IllegalArgumentException("Item require a phone");
        }
        String date = values.getAsString(ItemContract.ItemEntry.COLUMN_DATE);
        if (date == null){
            throw new IllegalArgumentException("Item require a date");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row now " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                rowDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                rowDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);

            case ITEMS_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs =new String[]{ String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ItemContract.ItemEntry.COLUMN_FIRST_NAME)){
            String firstName = values.getAsString(ItemContract.ItemEntry.COLUMN_FIRST_NAME);
            if (firstName == null){
                throw new IllegalArgumentException("Items require first name");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_LAST_NAME)){
            String lastName = values.getAsString(ItemContract.ItemEntry.COLUMN_LAST_NAME);
            if (lastName == null){
                throw new IllegalArgumentException("Items require last name");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_EMAIL_ID)){
            String email = values.getAsString(ItemContract.ItemEntry.COLUMN_EMAIL_ID);
            if (email == null){
                throw new IllegalArgumentException("Items require email");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_ADDRESS)){
            String address = values.getAsString(ItemContract.ItemEntry.COLUMN_ADDRESS);
            if (address == null){
                throw new IllegalArgumentException("Items require address");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_WEIGHT)){
            String weight = values.getAsString(ItemContract.ItemEntry.COLUMN_WEIGHT);
            if (weight == null){
                throw new IllegalArgumentException("Items require weight");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_HEIGHT)){
            String height = values.getAsString(ItemContract.ItemEntry.COLUMN_HEIGHT);
            if (height == null){
                throw new IllegalArgumentException("Items require height");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_PHONE)){
            String phone = values.getAsString(ItemContract.ItemEntry.COLUMN_PHONE);
            if (phone == null){
                throw new IllegalArgumentException("Items require phone");
            }
        }

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_DATE)){
            String date = values.getAsString(ItemContract.ItemEntry.COLUMN_DATE);
            if (date == null){
                throw new IllegalArgumentException("Items require date");
            }
        }

        if (values.size() == 0){
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowUpdate = db.update(ItemContract.ItemEntry.TABLE_NAME, values,selection, selectionArgs);

        if (rowUpdate != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdate;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ITEMS:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " with match " + match);
        }
    }
}

