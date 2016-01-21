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

        if (getIntent().getExtras() == null) {
            // if list to view in detail is not specified, go back to main page
            Intent intent = new Intent(ViewListDetailActivity.this, ViewListsActivity.class);
            startActivity(intent);
            return;
        }

        final String listName = getIntent().getExtras().getString("SELECTED_LIST_NAME");
        mTitle.setText(listName);
        ToDoList list = getListByName(listName);

        //TODO check if new item title & detail were passed via extras & add new item to list

        // populate the list view with item titles for the selected list
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

        //TODO set listener for "delete list" button (may need "are you sure" popup)

        // add item button
        View.OnClickListener addItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListDetailActivity.this, AddItemActivity.class);
                intent.putExtra("SELECTED_LIST_NAME", listName);
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
