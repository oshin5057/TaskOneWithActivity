package com.example.android.taskonewithactivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.android.taskonewithactivity.adapter.ItemAdapter;
import com.example.android.taskonewithactivity.data.ItemContract;
import com.example.android.taskonewithactivity.listener.ItemListener;
import com.example.android.taskonewithactivity.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemListener {

    private ItemAdapter mAdapter;

    private List<Item> items = new ArrayList<>();

    public static final int REQUEST_CODE = 100;

    FloatingActionButton mFAB;

    RecyclerView mRecyclerView;

    EditText mETSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFAB = findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);

        mETSearch = findViewById(R.id.et_search);
        mETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.getFilter().filter(s);
            }
        });

        fetchAllItems();
    }

    private void fetchAllItems() {

        Cursor cursor = null;

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

        try {
            items = new ArrayList<>();
            cursor = getContentResolver().query(ItemContract.ItemEntry.CONTENT_URI, projection, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                int idColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry._ID);
                int firstNameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_FIRST_NAME);
                int lastNameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_LAST_NAME);
                int emailColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_EMAIL_ID);
                int addressColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ADDRESS);
                int weightColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_WEIGHT);
                int heightColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_HEIGHT);
                int phoneColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_PHONE);
                int dateColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_DATE);

                int id = cursor.getInt(idColumnIndex);
                String firstName = cursor.getString(firstNameColumnIndex);
                String lastName = cursor.getString(lastNameColumnIndex);
                String email = cursor.getString(emailColumnIndex);
                String address = cursor.getString(addressColumnIndex);
                String weight = cursor.getString(weightColumnIndex);
                String height = cursor.getString(heightColumnIndex);
                String phone = cursor.getString(phoneColumnIndex);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = dateFormat.parse(cursor.getString(dateColumnIndex));

                Item item = new Item();
                item.cursorId = id;
                item.mFirstName = firstName;
                item.mLastName = lastName;
                item.mEmail = email;
                item.mAddress = address;
                item.weight = weight;
                item.height = height;
                item.phoneNo = phone;
                item.date = date;
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        mAdapter.setData(items);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fetchAllItems();
        }
    }

    @Override
    public void onDelete(int position, int cursorId) {
        Uri mCurrentItemUir = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, cursorId);
        getContentResolver().delete(mCurrentItemUir, null, null);
        items.remove(position);
        mAdapter.setData(items);
    }

    @Override
    public void onEdit(int position, int cursorId) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        Uri mCurrentItemUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, cursorId);
        intent.setData(mCurrentItemUri);
        startActivityForResult(intent, 100);
    }
}