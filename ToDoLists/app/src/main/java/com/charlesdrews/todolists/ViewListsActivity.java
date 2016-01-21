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

public class ViewListsActivity extends AppCompatActivity {

    public static final ArrayList<ToDoList> mToDoLists = new ArrayList<ToDoList>();
    private ListView mListView;
    private Button mAddListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lists);

        mListView = (ListView) findViewById(R.id.lists_list_view);
        mAddListButton = (Button) findViewById(R.id.add_list_button);

        //TODO make this use bundle
        // check if new list name passed via intent extras
        if (getIntent().getExtras() != null) {
            String newListName = getIntent().getExtras().getString("NEW_LIST_NAME");
            mToDoLists.add(new ToDoList(newListName));
        }

        // gather all existing list names and populate the list view
        ArrayList<String> listNames = getListNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ViewListsActivity.this,
                android.R.layout.simple_list_item_1,
                listNames);
        mListView.setAdapter(adapter);

        // make the "Add List" button send user to AddListActivity
        View.OnClickListener addListListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListsActivity.this, AddListActivity.class);
                startActivity(intent);
            }
        };
        mAddListButton.setOnClickListener(addListListener);

        // make clicking the ListView send user to ViewListDetailActivity
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewListsActivity.this, ViewListDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(getString(R.string.from_activity_key), "ViewListsActivity");
                extras.putString(getString(R.string.selected_list_key), ((TextView)view).getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        };
        mListView.setOnItemClickListener(onItemClickListener);
    }

    public static ArrayList<String> getListNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        for (ToDoList list : mToDoLists) {
            listNames.add(list.getName());
        }
        return listNames;
    }

}
