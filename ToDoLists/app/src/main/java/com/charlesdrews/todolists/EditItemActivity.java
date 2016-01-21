package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.charlesdrews.todolists.Constants.*;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    private EditText mListName; //TODO make this a spinner
    private EditText mItemTitle, mItemDetail;
    private Button mCancelButton, mDeleteButton, mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mListName = (EditText) findViewById(R.id.edit_item_list_name); //TODO make this a spinner
        mItemTitle = (EditText) findViewById(R.id.edit_item_title);
        mItemDetail = (EditText) findViewById(R.id.edit_item_detail);
        mCancelButton = (Button) findViewById(R.id.edit_item_cancel_button);
        mDeleteButton = (Button) findViewById(R.id.edit_item_delete_button);
        mUpdateButton = (Button) findViewById(R.id.edit_item_update_button);

        // check if extras included; without SELECTED_ITEM nothing for this activity to do
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            finish();
        }

        // get and display selected list name and selected item title
        final String originalListName = extrasReceived.getString(SELECTED_LIST);
        final String originalItemTitle = extrasReceived.getString(SELECTED_ITEM);
        mListName.setText(originalListName);
        mItemTitle.setText(originalItemTitle);

        // also get item detail via the atual list and item objects
        //TODO check if getListByName returns null
        final ToDoList list = ViewListDetailActivity.getListByName(originalListName);
        //TODO check if getItemByName returns null
        ToDoItem item = list.getItemByTitle(originalItemTitle);
        mItemDetail.setText(item.getDetail());

        // cancel button
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // delete button
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditItemActivity.this, ViewListDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(FROM_ACTIVITY, EDIT_ITEM_DELETE);
                extras.putString(SELECTED_LIST, originalListName);
                extras.putString(ORIGINAL_ITEM_TITLE, originalItemTitle);
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // update button
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedListName = mListName.getText().toString(); //TODO make this pull from a spinner
                String updatedItemTitle = mItemTitle.getText().toString();
                String updatedItemDetail = mItemDetail.getText().toString();

                if (updatedItemTitle.isEmpty()) {
                    mItemTitle.setError("Item title cannot be blank");
                } else if (isItemTitleOK(originalListName, updatedListName, originalItemTitle, updatedItemTitle)) {
                    // if new title is OK with specified list
                    Intent intent = new Intent(EditItemActivity.this, ViewListDetailActivity.class);
                    Bundle extras = new Bundle();
                    //TODO array or hashmap?
                    extras.putString(FROM_ACTIVITY, EDIT_ITEM_UPDATE);
                    extras.putString(ORIGINAL_LIST_NAME, originalListName);
                    extras.putString(ORIGINAL_ITEM_TITLE, originalItemTitle);
                    extras.putString(SELECTED_LIST, updatedListName);
                    extras.putString(UPDATED_ITEM_TITLE, updatedItemTitle);
                    extras.putString(UPDATED_ITEM_DETAIL, updatedItemDetail);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    // if new title is not OK
                    Toast.makeText(
                            EditItemActivity.this,
                            "There is already an item in that list with that title. Please enter a new title.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isItemTitleOK(String oldListName, String newListName, String oldTitle, String newTitle) {
        if (oldListName.equals(newListName)) { // list name did not change
            ToDoList oldList = ViewListDetailActivity.getListByName(oldListName);
            ArrayList<String> oldItemTitles = oldList.getItemTitles();
            oldItemTitles.remove(oldTitle); // don't want to reject title if it didn't change
            if (oldItemTitles.contains(newTitle)) {
                return false; // old list already has an item with title == newTitle
            } else {
                return true; // old list does not have an item with title == newTitle
            }
        } else {
            ToDoList newList = ViewListDetailActivity.getListByName(newListName);
            ArrayList<String> newItemTitles = newList.getItemTitles();
            if (newItemTitles.contains(newTitle)) {
                return false; // new list already has an item with title == newTitle
            } else {
                return true; // new list does not have an item with title == newTitle
            }
        }
    }
}
