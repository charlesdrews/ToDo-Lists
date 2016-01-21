package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        String listName = getIntent().getExtras().getString("SELECTED_LIST_NAME");
        mTitle.setText(listName);
        ToDoList list = getListByName(listName);

        // gather item titles for the selected list and populate the list view
        ArrayList<String> itemTitles = getItemTitles(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ViewListDetailActivity.this,
                android.R.layout.simple_list_item_1,
                itemTitles);
        mListView.setAdapter(adapter);

    }

    public static ToDoList getListByName(String name) {
        for (ToDoList list : ViewListsActivity.mToDoLists) {
            if (list.getName().equals(name)) {
                return list;
            }
        }
        return null;
    }

    public static ArrayList<String> getItemTitles(ToDoList list) {
        ArrayList<String> itemTitles = new ArrayList<String>();
        for (ToDoItem item : list.getItems()) {
            itemTitles.add(item.getTitle());
        }
        return itemTitles;
    }
}
