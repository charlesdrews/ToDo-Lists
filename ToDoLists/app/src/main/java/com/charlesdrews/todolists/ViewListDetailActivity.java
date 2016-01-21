package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewListDetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private ListView mListView;
    private Button mBackButton, mDeleteButton, mAddItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_detail);

        mTitle = (TextView) findViewById(R.id.list_detail_title);
        mListView = (ListView) findViewById(R.id.list_detail_list_view);
        mBackButton = (Button) findViewById(R.id.list_detail_back_button);
        mDeleteButton = (Button) findViewById(R.id.delete_list_button);
        mAddItemButton = (Button) findViewById(R.id.add_item_button);

        // check if extras included in intent - if not, go to home activity
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            // if list to view in detail is not specified, go back to main page
            Intent intent = new Intent(ViewListDetailActivity.this, ViewListsActivity.class);
            startActivity(intent);
            return;
        }

        // no matter which activity sent us here, the extras should specify the selected list
        // get name for selected list user wants to view and populate title
        final String listName = getIntent().getExtras().getString(getString(R.string.selected_list_key));
        mTitle.setText(listName);
        ToDoList list = getListByName(listName);

        // if new item info passed from AddItemActivityCreate then add item to list
        // (as opposed to from ViewListsActivity or AddItemActivityCancel)
        String fromActivity = extrasReceived.getString(getString(R.string.from_activity_key));
        if (fromActivity.equals("AddItemActivityCreate")) {
            String newItemTitle = getIntent().getExtras().getString("NEW_ITEM_TITLE");
            String newItemDetail = getIntent().getExtras().getString("NEW_ITEM_DETAIL");
            list.addItem(new ToDoItem(newItemTitle, newItemDetail, listName));
        }

        // populate list view with items for this list, now possibly including a new item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ViewListDetailActivity.this,
                android.R.layout.simple_list_item_1,
                list.getItemTitles());
        mListView.setAdapter(adapter);

        // back button
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListDetailActivity.this, ViewListsActivity.class);
                startActivity(intent);
            }
        };
        mBackButton.setOnClickListener(backListener);

        // add delete list button
        View.OnClickListener deleteListListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO set listener for "delete list" button (may need "are you sure" popup)

            }
        };
        mDeleteButton.setOnClickListener(deleteListListener);

        // add item button
        View.OnClickListener addItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListDetailActivity.this, AddItemActivity.class);
                Bundle extras = new Bundle();
                extras.putString(getString(R.string.selected_list_key), listName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        };
        mAddItemButton.setOnClickListener(addItemListener);

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
