package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.charlesdrews.todolists.Constants.*;

public class AddListActivity extends AppCompatActivity {

    private EditText mInput;
    private Button mCreateButton, mCancelButton;

    /**
     * Create/populate views for the Add List activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        mInput = (EditText) findViewById(R.id.list_name_input);
        mCreateButton = (Button) findViewById(R.id.create_new_list_button);
        mCancelButton = (Button) findViewById(R.id.cancel_new_list_button);

        // Create button sends data back
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = mInput.getText().toString();
                if (listName.isEmpty()) {
                    mInput.setError(BLANK_LIST_NAME_MSG);
                } else if (ViewListsActivity.getListNames().contains(listName)) {
                    Toast.makeText(AddListActivity.this, LIST_NAME_IN_USE_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AddListActivity.this, ViewListsActivity.class);
                    intent.putExtra(NEW_LIST_NAME, listName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        // Cancel button indicates no data passed back as result
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
