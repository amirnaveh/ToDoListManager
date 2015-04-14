package il.ac.huji.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import static il.ac.huji.todolist.Constants.FIRST_COLUMN;
import static il.ac.huji.todolist.Constants.NO_DATE_IDENTIFIER;
import static il.ac.huji.todolist.Constants.SECOND_COLUMN;

/**
 * This class is an activity dialog for adding a new task to the To-Do List
 */
public class AddNewTodoItemActivity extends Activity implements OnClickListener {

    EditText txt;
    DatePicker datePicker;
    Button ok;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(il.ac.huji.todolist.R.layout.activity_add_new_todo_item);

        txt = (EditText) findViewById(R.id.edtNewItem);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        ok = (Button) findViewById(R.id.btnOK);
        cancel = (Button) findViewById(R.id.btnCancel);

        datePicker.setCalendarViewShown(false);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK: // Add the new task using intent extras
                Intent intent = getIntent();
                Bundle extras = new Bundle();

                String task = txt.getText().toString();

                if (verifyTaskText(task)) {
                    extras.putString(FIRST_COLUMN, task);
                } else {
                    break;
                }

                Date date = getDateFromDatePicker();
                if (date == null) {
                    extras.putLong(SECOND_COLUMN, NO_DATE_IDENTIFIER);
                } else {
                    extras.putLong(SECOND_COLUMN, date.getTime());
                }

                intent.putExtras(extras);

                setResult(Activity.RESULT_OK, intent);
                this.finish();

            case R.id.btnCancel: // return with no action
                setResult(Activity.RESULT_CANCELED);
                this.finish();

            default:
                setResult(Activity.RESULT_CANCELED);
                this.finish();
        }
    }

    /**
     * Verifies that the task text is not null (empty). In case it is, it also invokes a dialog
     *
     * @param text - the task text
     * @return true if the text is valid, false if null
     */
    private boolean verifyTaskText(String text) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (text.isEmpty()) {
            builder.setTitle("Error");
            builder.setMessage("Can't add an empty task");
            final TextView errorText = new TextView(this);
            builder.setView(errorText);
            builder.setNeutralButton("Ok", null);
            builder.create().show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Extracts the date chosen by the user from the DatePicker
     *
     * @return the date in form of java.util.Date
     */
    private Date getDateFromDatePicker() {
        if (datePicker == null) {
            return null;
        }
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
