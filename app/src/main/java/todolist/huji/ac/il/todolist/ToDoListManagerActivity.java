package todolist.huji.ac.il.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ToDoListManagerActivity extends ActionBarActivity {

    ArrayAdapter adapter;
    TextView textView;
    ListView listView;
    ArrayList taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_manager);

        textView = (TextView) findViewById(R.id.menuItemDelete);

        listView = (ListView) findViewById(R.id.lstTodoItems);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);
                view.setSelected(true);
                deleteTask(position);
                return true;
            }
        });

        taskList = new ArrayList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuItemAdd) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            EditText taskText = (EditText) findViewById(R.id.edtNewItem);
            String task = taskText.getText().toString();

            if (task.isEmpty()) {
                builder.setTitle("Error");
                builder.setMessage("Can't add an empty task");
                final TextView errorText = new TextView(this);
                builder.setView(errorText);
                builder.setNeutralButton("Ok", null);
                Log.d("ToDoListManagerActivity", "Empty task");
                builder.create().show();
                return true;
            }
            else {
                addTask(task);
                taskText.setText(null);
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds the task to the list and calls the updateUI method.
     * @param task the task to add (text)
     */
    private void addTask (String task) {
        taskList.add(task);
        updateUI();
    }

    /**
     * This method updates the UI.
     * It includes an overriding getView method for the adapter, to implement the alternating text
     * color
     */
    private void updateUI () {

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList){

            // Overriding the getView method to set the alternating Red and Blue colors.
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                if (position % 2 == 1) {
                    textView.setTextColor(Color.BLUE);
                }
                else {
                    textView.setTextColor(Color.RED);
                }

                return view;
            }
        };
        listView.setAdapter(adapter);

        ListView list = (ListView) findViewById(R.id.lstTodoItems);

    }

    /**
     * Deletes the task from the list and calls updateUI method
     * @param taskId the ID of the task to delete
     */
    private void deleteTask (final int taskId) {
        if (taskId >= 0 ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView taskDelete = (TextView) findViewById(R.id.menuItemDelete);
            final String task = (String) listView.getItemAtPosition(taskId);

            builder.setTitle(task);
            builder.setNeutralButton("Delete Item", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("ToDoListManagerActivity", ("Delete task: " + task));
                    removeFromList(taskId);
                    updateUI();
                }
            });

            builder.create().show();

        }
    }

    /**
     * Actually removes the task from the ArrayList
     * @param taskId the ID of the task to delete
     */
    private void removeFromList(int taskId) {
        this.taskList.remove(taskId);
    }

}
