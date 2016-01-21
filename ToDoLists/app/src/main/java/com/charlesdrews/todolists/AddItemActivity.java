package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    private EditText mTitleInput, mDetailInput;
    private Button mCreateButton, mCancelButton;
    private TextView mListName; //TODO make this a spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mTitleInput = (EditText) findViewById(R.id.item_title_input);
        mDetailInput = (EditText) findViewById(R.id.item_detail_input);
        mCreateButton = (Button) findViewById(R.id.create_new_item_button);
        mCancelButton = (Button) findViewById(R.id.cancel_new_item_button);
        mListName = (TextView) findViewById(R.id.add_item_list_name); //TODO make this a spinner

        // check if extras included in intent - if not, go to home activity
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            // if selected list is not specified, go back to main page
            Intent intent = new Intent(AddItemActivity.this, ViewListsActivity.class);
            startActivity(intent);
            return;
        }

        // no matter which activity sent us here, the extras should specify the selected list
        // get name for selected list user wants to view and populate title //TODO make the title a spinner
        final String listName = getIntent().getExtras().getString(getString(R.string.selected_list_key));
        mListName.setText(listName);
        final ToDoList list = ViewListDetailActivity.getListByName(listName);

        // create item button
        View.OnClickListener createListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTitle = mTitleInput.getText().toString();
                if (itemTitle.isEmpty()) {
                    mTitleInput.setError("Item title cannot be blank");
                } else if (list.getItemTitles().contains(itemTitle)) { //TODO update this to use whatever list is in the spinner
                    Toast.makeText(
                            AddItemActivity.this,
                            "There is already an item in this list with that title. Please enter a new title.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent createIntent = new Intent(AddItemActivity.this, ViewListDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(getString(R.string.from_activity_key), "AddItemActivityCreate");
                    extras.putString(getString(R.string.selected_list_key), listName);
                    extras.putString("NEW_ITEM_TITLE", itemTitle);
                    extras.putString("NEW_ITEM_DETAIL", mDetailInput.getText().toString());
                    createIntent.putExtras(extras);
                    startActivity(createIntent);
                }
            }
        };
        mCreateButton.setOnClickListener(createListener);

        // cancel button
        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, ViewListDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(getString(R.string.from_activity_key), "AddItemActivityCancel");
                extras.putString(getString(R.string.selected_list_key), listName);
                intent.putExtras(extras);
                startActivity(intent);
            }
        };
        mCancelButton.setOnClickListener(cancelListener);
    }
}
