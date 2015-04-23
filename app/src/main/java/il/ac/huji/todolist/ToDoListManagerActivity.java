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

import java.util.Date;
import java.util.List;

import static il.ac.huji.todolist.Constants.FIRST_COLUMN;
import static il.ac.huji.todolist.Constants.SECOND_COLUMN;


public class ToDoListManagerActivity extends ActionBarActivity {

    private static final int ADD_TASK_REQUEST = 1;
    private static final String CALL_STRING = "Call ";

    ListView listView;
    ToDoListDbHelper db;
    public List<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_manager);

        db = new ToDoListDbHelper(this);

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

            Task task = list.get(info.position);

            menu.setHeaderTitle(task.getTask());

            MenuInflater inflater = getMenuInflater();
            if (task.getTask().startsWith(CALL_STRING)) { // Context menu with call option
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
                Task task = list.get(info.position);
                String taskTxt = task.getTask();

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
        db.removeTask(list.get(position));
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
                String taskText = extras.getString(FIRST_COLUMN);
                Date dueDate = new Date(extras.getLong(SECOND_COLUMN));

                Task task = new Task(taskText, dueDate.getTime());
                task.setId(db.addTask(task));

                updateUI();
            }
        }
    }

    /**
     * This method updates the UI, updating the screen.
     */
    private void updateUI() {
        list = db.getAllTasks();
        ListViewAdapter adapter = new ListViewAdapter(this, list);
        listView.setAdapter(adapter);
    }

}
