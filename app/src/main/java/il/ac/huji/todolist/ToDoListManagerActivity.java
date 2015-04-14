package il.ac.huji.todolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static il.ac.huji.todolist.Constants.FIRST_COLUMN;
import static il.ac.huji.todolist.Constants.NO_DATE_IDENTIFIER;
import static il.ac.huji.todolist.Constants.NO_DATE_STR;
import static il.ac.huji.todolist.Constants.SECOND_COLUMN;


public class ToDoListManagerActivity extends ActionBarActivity {

    private static final int ADD_TASK_REQUEST = 1;
    private static final String CALL_STRING = "Call ";

    ListView listView;
    public ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_manager);

        list = new ArrayList<>();

        listView = (ListView) findViewById(R.id.lstTodoItems);

        registerForContextMenu(listView);

        updateUI();

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

        switch (item.getItemId()) {
            case R.id.menuItemAdd:
                addTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lstTodoItems) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            HashMap<String, String> task = list.get(info.position);

            String taskText = task.get(FIRST_COLUMN);
            menu.setHeaderTitle(taskText);

            MenuInflater inflater = getMenuInflater();
            if (taskText.startsWith(CALL_STRING)) { // Context menu with call option
                inflater.inflate(R.menu.menu_context_call, menu);
            } else { // Context menu without call option
                inflater.inflate(R.menu.menu_context, menu);
            }

        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();


        switch (item.getItemId()) {
            case R.id.menuItemDelete:
                deleteTask(info.position);
                return true;

            case R.id.menuItemCall:
                HashMap<String, String> task = list.get(info.position);
                String taskTxt = task.get(FIRST_COLUMN);

                // Parse the task to a fitting URI string for the Dial intent
                String phoneNum = taskTxt.replaceAll("[^0-9-]", "");
                String call = "tel:" + phoneNum;

                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(call));
                startActivity(dial);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This method deletes a task from the list and listview
     * @param position the position of the task to be deleted
     */
    private void deleteTask(int position) {
        list.remove(position);
        updateUI();
    }

    /**
     * This method adds a task to the list and listview
     */
    private void addTask() {
        Intent intent = new Intent("il.ac.huji.todolist.AddNewTodoItemActivity");
        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_TASK_REQUEST) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String task = extras.getString(FIRST_COLUMN);
                Date dueDate = new Date(extras.getLong(SECOND_COLUMN));

                HashMap<String, String> temp = new HashMap<>();

                temp.put(FIRST_COLUMN, task);

                if (dueDate.getTime() == NO_DATE_IDENTIFIER) {
                    temp.put(SECOND_COLUMN, NO_DATE_STR);
                } else {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    temp.put(SECOND_COLUMN, df.format(dueDate));
                }

                list.add(temp);

                updateUI();
            }
        }
    }

    /**
     * This method updates the UI, updating the screen.
     */
    private void updateUI() {
        ListViewAdapter adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
    }

}
