package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is a helper class for handling the DB for the To-Do List Manager app
 */
public class ToDoListDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "To-DoListManager"; // Database name
    private static final String TABLE_TASKS = "tasks"; // tasks table name

    // tasks Table columns names
    private static final String KEY_ID = BaseColumns._ID;
    private static final String KEY_TASKNAME = "task";
    private static final String KEY_DUEDATE = "dueDate";

    private static final String SELECT_QUERY = "SELECT * FROM " + TABLE_TASKS;

    public ToDoListDbHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_TASKS +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TASKNAME + " TEXT, " +
                KEY_DUEDATE + " TEXT)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASKS);
        // Create tables again
        onCreate(db);

    }

    public long addTask (Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASKNAME, task.getTask()); // task name
        values.put(KEY_DUEDATE, task.getDueDate()); // task due date

        long id = db.insert(TABLE_TASKS, null, values); // Inserting row

        db.close(); // Closing database connection

        return id;

    }

    public void removeTask (Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        String delete = "DELETE FROM " + TABLE_TASKS + " WHERE " +
                KEY_ID + " = '" + task.getId() + "'";

        db.execSQL(delete);

        db.close(); // Closing database connection

    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(0));
                task.setTask(cursor.getString(1));
                task.setDueDate(cursor.getString(2));

                // Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return task list
        return taskList;
    }

}
