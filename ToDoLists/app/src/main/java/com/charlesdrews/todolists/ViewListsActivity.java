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
import static com.charlesdrews.todolists.Constants.*;

import java.util.ArrayList;

public class ViewListsActivity extends AppCompatActivity {

    private ListView mListView;
    private Button mAddListButton;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mListNames;
    public static final ArrayList<ToDoList> mToDoLists = new ArrayList<ToDoList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lists);

        mListView = (ListView) findViewById(R.id.lists_list_view);
        mAddListButton = (Button) findViewById(R.id.add_list_button);

        mListNames = getListNames();
        mAdapter = new ArrayAdapter<String>(ViewListsActivity.this,
                android.R.layout.simple_list_item_1, mListNames);
        mListView.setAdapter(mAdapter);

        // ADD LIST button - expect result from AddListActivity
        mAddListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListsActivity.this, AddListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        // clicking the ListView sends user to ViewListDetailActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewListsActivity.this, ViewListDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(SELECTED_LIST, ((TextView)view).getText().toString()); // send name of list user clicked
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String newListName = data.getExtras().getString(NEW_LIST_NAME);
            mToDoLists.add(new ToDoList(newListName));
            mListNames.clear();
            mListNames.addAll(getListNames());
            mAdapter.notifyDataSetChanged();
        }
    }

    public static ArrayList<String> getListNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        for (ToDoList list : mToDoLists) {
            listNames.add(list.getName());
        }
        return listNames;
    }
}
