package com.sonalipandey.ado;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class ToDo extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayList<ToDoItem> itemlist;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE = 20;

    PracticeDatabaseHelper dbHelper = new PracticeDatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        //Add list
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        //add list to adapter
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //on click - retrieve the item
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ToDo.this, "Item position clicked: " + items.get(position), Toast.LENGTH_SHORT).show();
                //Send data to Edit activity
                Intent edit_activity = new Intent(ToDo.this, EditItemActivity.class);
                edit_activity.putExtra("position", position);
                edit_activity.putExtra("text", items.get(position));
                edit_activity.putExtra("id", itemlist.get(position).getId());
                startActivityForResult(edit_activity, REQUEST_CODE);
            }
        });
        setUpListViewListener();
    }

    public void onAddTask(View view) {
        EditText etEnterItem = (EditText) findViewById(R.id.etEnterItem);
        String itemText =  etEnterItem.getText().toString();
        itemsAdapter.add(itemText);
        //Create a new db instance on ToDoItem
        ToDoItem saveItem = new ToDoItem();
        if(!TextUtils.isEmpty(itemText)) {
            saveItem.setItemName(itemText);
            //Put item in DB
            long id = cupboard().withDatabase(dbHelper.getWritableDatabase()).put(saveItem);
            //Set the id in the object
            saveItem.setId(id);
            itemlist.add(saveItem);
        }
        //Refresh the Arraylist
        itemsAdapter .notifyDataSetChanged();
        etEnterItem.setText("");
    }

    @Override
    public void onResume(){
        super.onResume();
        itemlist = new ArrayList<ToDoItem>();
        items = new ArrayList<String>();
        //Get items from the DB
        Cursor toDoItem = cupboard().withDatabase(dbHelper.getWritableDatabase()).query(ToDoItem.class).getCursor();
        try {
            QueryResultIterable<ToDoItem> itr = cupboard().withCursor(toDoItem).iterate(ToDoItem.class);
            for (ToDoItem item : itr) {
                itemlist.add(item);
                items.add(item.getItemName());
            }
            itemsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            lvItems.setAdapter(itemsAdapter);
            itemsAdapter.notifyDataSetChanged();

        } finally {
            // close the cursor
            toDoItem.close();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position");


            // Toast the name to display temporarily on screen
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        }
    }

    private void setUpListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        Long itemIdToDelete = itemlist.get(pos).getId();
                        cupboard().withDatabase(dbHelper.getWritableDatabase()).delete(ToDoItem.class, itemIdToDelete);
                        itemlist.remove(pos);
                        items.remove(pos);
                        //finish();
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

    
}
