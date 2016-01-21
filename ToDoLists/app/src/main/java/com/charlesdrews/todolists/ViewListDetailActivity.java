package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.charlesdrews.todolists.Constants.*;

public class ViewListDetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private ListView mListView;
    private Button mDeleteButton, mAddItemButton;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItemTitles;
    private String mListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_detail);

        mTitle = (TextView) findViewById(R.id.list_detail_title);
        mListView = (ListView) findViewById(R.id.list_detail_list_view);
        mDeleteButton = (Button) findViewById(R.id.delete_list_button);
        mAddItemButton = (Button) findViewById(R.id.add_item_button);

        // check if extras included; without SELECTED_LIST nothing for this activity to do
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            finish();
        }

        // no matter which activity sent us here, the extras should specify the selected list
        // get name for selected list user wants to view and populate title
        mListName = extrasReceived.getString(SELECTED_LIST);
        mTitle.setText(mListName);
        //TODO check if getListByName returns null
        ToDoList list = getListByName(mListName);


        // populate list view with items for this list, now possibly including a new item
        mItemTitles = list.getItemTitles();
        mAdapter = new ArrayAdapter<String>(ViewListDetailActivity.this,
                android.R.layout.simple_list_item_1, mItemTitles);
        mListView.setAdapter(mAdapter);

        // clicking the ListView sends user to EditItemActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewListDetailActivity.this, EditItemActivity.class);
                Bundle extras = new Bundle();
                extras.putString(SELECTED_LIST, mListName);
                extras.putString(SELECTED_ITEM, ((TextView) view).getText().toString()); // pass title of item user clicked
                intent.putExtras(extras);
                startActivityForResult(intent, 0);
            }
        });

        // delete list button
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO set listener for "delete list" button (may need "are you sure" popup)
            }
        });

        // add item button
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListDetailActivity.this, AddItemActivity.class);
                Bundle extras = new Bundle();
                extras.putString(SELECTED_LIST, mListName);
                intent.putExtras(extras);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ToDoList list = getListByName(data.getExtras().getString(SELECTED_LIST));
            String fromActivity = data.getExtras().getString(FROM_ACTIVITY);
            switch (fromActivity) {
                case ADD_ITEM_CREATE:
                    String newItemListName = data.getExtras().getString(SELECTED_LIST);
                    String newItemTitle = data.getExtras().getString(NEW_ITEM_TITLE);
                    String newItemDetail = data.getExtras().getString(NEW_ITEM_DETAIL);
                    list.addItem(new ToDoItem(newItemTitle, newItemDetail, newItemListName));
                    break;
                case EDIT_ITEM_DELETE:
                    String itemToDeleteTitle = data.getExtras().getString(ORIGINAL_ITEM_TITLE);
                    ToDoItem itemToDelete = list.getItemByTitle(itemToDeleteTitle);
                    list.removeItem(itemToDelete);
                    break;
                case EDIT_ITEM_UPDATE:
                    String originalListName = data.getExtras().getString(ORIGINAL_LIST_NAME);
                    String originalItemTitle = data.getExtras().getString(ORIGINAL_ITEM_TITLE);
                    String updatedListName = data.getExtras().getString(SELECTED_LIST);
                    String updatedItemTitle = data.getExtras().getString(UPDATED_ITEM_TITLE);
                    String updatedItemDetail = data.getExtras().getString(UPDATED_ITEM_DETAIL);
                    if (updatedListName.equals(originalListName)) {
                        // list name did not change; update item w/in list
                        //TODO check if getListByName returns null
                        ToDoItem updatedItem = list.getItemByTitle(originalItemTitle);
                        updatedItem.setTitle(updatedItemTitle);
                        updatedItem.setDetail(updatedItemDetail);
                    } else {
                        // list name did change; delete item from original list
                        ToDoList originalList = getListByName(originalListName);
                        originalList.removeItem(originalList.getItemByTitle(originalItemTitle));
                        // add item to list specified by updatedListName (already referenced by "list")
                        list.addItem(new ToDoItem(updatedItemTitle, updatedItemDetail, updatedListName));
                    }
                    break;
            }
            mListName = list.getName();
            mTitle.setText(mListName);
            mItemTitles.clear();
            mItemTitles.addAll(list.getItemTitles());
            mAdapter.notifyDataSetChanged();
        }
    }

    public static ToDoList getListByName(String name) {
        for (ToDoList list : ViewListsActivity.mToDoLists) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }
}
