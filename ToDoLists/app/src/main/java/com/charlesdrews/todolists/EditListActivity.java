package com.charlesdrews.todolists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.charlesdrews.todolists.Constants.*;

public class EditListActivity extends AppCompatActivity {

    EditText mListNameInput;
    Button mCancel, mDelete, mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        // check if extras included; without SELECTED_LIST nothing for this activity to do
        Bundle extrasReceived = getIntent().getExtras();
        if (extrasReceived == null) {
            finish();
        }

        mListNameInput = (EditText) findViewById(R.id.edit_list_name_input);
        mCancel = (Button) findViewById(R.id.edit_list_cancel);
        mDelete = (Button) findViewById(R.id.edit_list_delete);
        mUpdate = (Button) findViewById(R.id.edit_list_update);

        final String originalListName = extrasReceived.getString(SELECTED_LIST);
        mListNameInput.setText(originalListName);

        // cancel button
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // delete button
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Intent intent = new Intent(EditListActivity.this, ViewListsActivity.class);
                Bundle extras = new Bundle();
                extras.putString(FROM_ACTIVITY, EDIT_LIST_DELETE);
                extras.putString(SELECTED_LIST, mListNameInput.getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        // update button
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedListName = mListNameInput.getText().toString();
                if (updatedListName.isEmpty()) {
                    mListNameInput.setError("Item title cannot be blank");
                } else if (ViewListsActivity.getListNames().contains(updatedListName)) {
                    Toast.makeText(EditListActivity.this, LIST_NAME_IN_USE_MSG, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(EditListActivity.this, ViewListDetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(FROM_ACTIVITY, EDIT_LIST_UPDATE);
                    extras.putString(SELECTED_LIST, originalListName);
                    extras.putString(NEW_LIST_NAME, updatedListName);
                    intent.putExtras(extras);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
