package com.sonalipandey.ado;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class EditItemActivity extends AppCompatActivity {
    private final int RESULT_OK = 200;
    Long dbId;
    ToDoItem dbItem;

    PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText etEditText = (EditText) findViewById(R.id.etEditItem);

        Intent text = getIntent();
        Bundle b = text.getExtras();
        if(b != null){
            //String itemtext = (String) b.get("text");
            //etEditText.setText(itemtext);
            //etEditText.setSelection(etEditText.getText().length());
            //Get id from db
            dbId = (Long) b.get("id");
        }
        //get item from db with id
        dbItem = cupboard().withDatabase(dbHelper.getWritableDatabase()).get(ToDoItem.class, dbId);
        String itemtext = (String)dbItem.getItemName();
        etEditText.setText(itemtext);
        etEditText.setSelection(etEditText.getText().length());
    }

    public void onSave(View v){
        EditText etText = (EditText) findViewById(R.id.etEditItem);
        dbItem.setItemName(etText.getText().toString());
        //Save item
        cupboard().withDatabase(dbHelper.getWritableDatabase()).put(dbItem);
        Intent data = new Intent();
        //data.putExtra("text", etText.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    public void onDelete(View v){
        //delete item
        cupboard().withDatabase(dbHelper.getWritableDatabase()).delete(ToDoItem.class, dbId);
        Intent data = new Intent();
        //data.putExtra("text", etText.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
