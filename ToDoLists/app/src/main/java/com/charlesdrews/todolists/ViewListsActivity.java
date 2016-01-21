package com.charlesdrews.todolists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewListsActivity extends AppCompatActivity {

    public ArrayList<ToDoList> mToDoLists;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lists);

        mToDoLists = new ArrayList<ToDoList>();
        mToDoLists.add(new ToDoList("list one"));
        mToDoLists.add(new ToDoList("list two"));

        mListView = (ListView) findViewById(R.id.lists_list_view);

        ArrayList<String> listNames = new ArrayList<String>();
        for (ToDoList list : mToDoLists) {
            listNames.add(list.getName());
        }
        mAdapter = new ArrayAdapter<String>(ViewListsActivity.this, android.R.layout.simple_list_item_1, listNames);
        mListView.setAdapter(mAdapter);
    }
}
