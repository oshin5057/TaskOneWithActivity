package com.example.android.taskonewithactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.android.taskonewithactivity.data.ItemContract;
import com.google.android.material.textfield.TextInputEditText;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_NOTE_LOADER = 0;

    private Uri mCurrentNoteUri;

    private TextInputEditText mFirstNameEditText;
    private TextInputEditText mLastNameEditText;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mAddressEditText;
    private TextInputEditText mWeightEditText;
    private TextInputEditText mHeightEditText;
    private TextInputEditText mPhoneEditText;
    private TextInputEditText mDateEditText;

    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();

        if (mCurrentNoteUri == null){
            setTitle("Add Item");

            invalidateOptionsMenu();
        }
        else {
            setTitle("Edit Item");

            getSupportLoaderManager().initLoader(EXISTING_NOTE_LOADER, null,this);
        }

        mFirstNameEditText = findViewById(R.id.text_ip_et_first_name);
        mLastNameEditText = findViewById(R.id.text_ip_et_last_name);
        mEmailEditText = findViewById(R.id.text_ip_et_email);
        mAddressEditText = findViewById(R.id.text_ip_et_address);
        mWeightEditText = findViewById(R.id.text_ip_et_weight);
        mHeightEditText = findViewById(R.id.text_ip_et_height);
        mPhoneEditText = findViewById(R.id.text_ip_et_phone_no);
        mDateEditText = findViewById(R.id.text_ip_et_date);

        mFirstNameEditText.setOnTouchListener(mTouchListener);
        mLastNameEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mAddressEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mHeightEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mDateEditText.setOnTouchListener(mTouchListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveNote();
                finish();
                return true;
            case android.R.id.home:
                if(mItemHasChanged){
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String firstName = mFirstNameEditText.getText().toString().trim();
        String lastName = mLastNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String address = mAddressEditText.getText().toString().trim();
        String weight = mWeightEditText.getText().toString().trim();
        String height = mHeightEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();
        String date = mDateEditText.getText().toString().trim();

        if (mCurrentNoteUri == null && TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName)
                && TextUtils.isEmpty(email) && TextUtils.isEmpty(address) && TextUtils.isEmpty(weight)
                && TextUtils.isEmpty(height) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(date)){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_FIRST_NAME, firstName);
        values.put(ItemContract.ItemEntry.COLUMN_LAST_NAME, lastName);
        values.put(ItemContract.ItemEntry.COLUMN_EMAIL_ID, email);
        values.put(ItemContract.ItemEntry.COLUMN_ADDRESS, address);
        values.put(ItemContract.ItemEntry.COLUMN_WEIGHT, weight);
        values.put(ItemContract.ItemEntry.COLUMN_HEIGHT, height);
        values.put(ItemContract.ItemEntry.COLUMN_PHONE, phone);
        values.put(ItemContract.ItemEntry.COLUMN_DATE, date);

        if (mCurrentNoteUri == null){
            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
            if (newUri == null){
                Toast.makeText(this, "Error with saving Items", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
            else {
                Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }
        else {
            int rowAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
            if (rowAffected == 0){
                Toast.makeText(this, "Error with updating Item", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
            }
            else {
                Toast.makeText(this, "Item Updated", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }

        setResult(RESULT_OK);
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_FIRST_NAME,
                ItemContract.ItemEntry.COLUMN_LAST_NAME,
                ItemContract.ItemEntry.COLUMN_EMAIL_ID,
                ItemContract.ItemEntry.COLUMN_ADDRESS,
                ItemContract.ItemEntry.COLUMN_WEIGHT,
                ItemContract.ItemEntry.COLUMN_HEIGHT,
                ItemContract.ItemEntry.COLUMN_PHONE,
                ItemContract.ItemEntry.COLUMN_DATE
        };

        return new CursorLoader(this,
                mCurrentNoteUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()){
            int firstNameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_FIRST_NAME);
            int lastNameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_LAST_NAME);
            int emailColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_EMAIL_ID);
            int addressColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ADDRESS);
            int weightColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_WEIGHT);
            int heightColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_HEIGHT);
            int phoneColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PHONE);
            int dateColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_DATE);

            String firstName = cursor.getString(firstNameColumnIndex);
            String lastName = cursor.getString(lastNameColumnIndex);
            String email = cursor.getString(emailColumnIndex);
            String address = cursor.getString(addressColumnIndex);
            Double weight = cursor.getDouble(weightColumnIndex);
            Double height = cursor.getDouble(heightColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);
            String date = cursor.getString(dateColumnIndex);

            mFirstNameEditText.setText(firstName);
            mLastNameEditText.setText(lastName);
            mEmailEditText.setText(email);
            mAddressEditText.setText(address);
            mWeightEditText.setText(Double.toString(weight));
            mHeightEditText.setText(Double.toString(height));
            mPhoneEditText.setText(phone);
            mDateEditText.setText(date);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mFirstNameEditText.setText("");
        mLastNameEditText.setText("");
        mEmailEditText.setText("");
        mAddressEditText.setText("");
        mWeightEditText.setText("");
        mHeightEditText.setText("");
        mPhoneEditText.setText("");
        mDateEditText.setText("");
    }
}