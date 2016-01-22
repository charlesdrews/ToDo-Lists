package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.charlesdrews.todolists.Constants.*;


public class AddItemActivity extends AppCompatActivity {

    private EditText mTitleInput, mDetailInput;
    private Button mCreateButton, mCancelButton;
    private Spinner mSpinner;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // check if extras included; without SELECTED_LIST nothing for this activity to do
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            finish();
        }

        mTitleInput = (EditText) findViewById(R.id.item_title_input);
        mDetailInput = (EditText) findViewById(R.id.item_detail_input);
        mCreateButton = (Button) findViewById(R.id.create_new_item_button);
        mCancelButton = (Button) findViewById(R.id.cancel_new_item_button);

        mSpinner = (Spinner) findViewById(R.id.add_item_spinner);
        ArrayList<String> listNames = ViewListsActivity.getListNames();
        mAdapter = new ArrayAdapter<String>(AddItemActivity.this,
                android.R.layout.simple_spinner_item, listNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        final String listName = extrasReceived.getString(SELECTED_LIST);
        mSpinner.setSelection(listNames.indexOf(listName));

        //TODO check if getListByName returns null
        final ToDoList list = ViewListDetailActivity.getListByName(listName);

        // create item button sends data back to ViewListDetailActivity as a result
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTitle = mTitleInput.getText().toString();
                String itemListName = mSpinner.getSelectedItem().toString();
                ToDoList itemList = ViewListDetailActivity.getListByName(itemListName);

                if (itemTitle.isEmpty()) {
                    mTitleInput.setError(BLANK_ITEM_TITLE_MSG);
                } else if (itemList.getItemTitles().contains(itemTitle)) {
                    Toast.makeText(AddItemActivity.this, ITEM_TITLE_IN_USE_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AddItemActivity.this, ViewListDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(FROM_ACTIVITY, ADD_ITEM_CREATE);
                    extras.putString(SELECTED_LIST, itemListName);
                    extras.putString(NEW_ITEM_TITLE, itemTitle);
                    extras.putString(NEW_ITEM_DETAIL, mDetailInput.getText().toString());
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        // cancel button sends NO data back to ViewListDetailActivity as a result
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
