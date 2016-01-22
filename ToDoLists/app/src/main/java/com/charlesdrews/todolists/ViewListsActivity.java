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

    /**
     * This is the "Home" activity - create/populate views for the View Lists activity.
     * User can click a list to view list items in detail or can add a new list.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lists);

        mListView = (ListView) findViewById(R.id.lists_list_view);
        mAddListButton = (Button) findViewById(R.id.add_list_button);

        // if we arrive here from the delete button in the EditList activity, need to delete
        // the specified list.
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived != null && extrasReceived.getString(FROM_ACTIVITY).equals(EDIT_LIST_DELETE)) {
            String listToDeleteName = extrasReceived.getString(SELECTED_LIST);
            ToDoList listToDelete = ViewListDetailActivity.getListByName(listToDeleteName);
            mToDoLists.remove(listToDelete);
        }

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
                extras.putString(SELECTED_LIST, ((TextView) view).getText().toString()); // send name of list user clicked
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    /**
     * Receives data from the Add List activity & adds new list to ArrayList & adapter.
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * In some cases this activity was showing previously deleted lists when user gets here
     * via the back button. Solved this by refreshing the adapter upon resume.
     */
    @Override
    protected void onResume() {
        super.onResume();

        mListNames.clear();
        mListNames.addAll(getListNames());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Returns a collection of Strings representing the names of each list.
     * @return ArrayList<String> containing the names of all the ToDoList objects.
     */
    public static ArrayList<String> getListNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        for (ToDoList list : mToDoLists) {
            listNames.add(list.getName());
        }
        return listNames;
    }
}
