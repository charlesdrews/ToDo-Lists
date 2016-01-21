package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddListActivity extends AppCompatActivity {

    private EditText mInput;
    private Button mCreateButton, mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        mInput = (EditText) findViewById(R.id.list_name_input);
        mCreateButton = (Button) findViewById(R.id.create_new_list_button);
        mCancelButton = (Button) findViewById(R.id.cancel_new_list_button);

        View.OnClickListener createListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = mInput.getText().toString();
                if (listName.isEmpty()) {
                    mInput.setError("Name cannot be blank");
                } else if (ViewListsActivity.getListNames().contains(listName)) {
                    Toast.makeText(
                            AddListActivity.this,
                            "There is already a list with that name. Please enter a different name.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent createIntent = new Intent(AddListActivity.this, ViewListsActivity.class);
                    createIntent.putExtra("NEW_LIST_NAME", listName);
                    startActivity(createIntent);
                }
            }
        };
        mCreateButton.setOnClickListener(createListener);

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(AddListActivity.this, ViewListsActivity.class);
                startActivity(cancelIntent);
            }
        };
        mCancelButton.setOnClickListener(cancelListener);
    }
}
