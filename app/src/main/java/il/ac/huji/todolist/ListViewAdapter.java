package il.ac.huji.todolist;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static il.ac.huji.todolist.Constants.NO_DATE_IDENTIFIER;
import static il.ac.huji.todolist.Constants.NO_DATE_STR;

public class ListViewAdapter extends BaseAdapter {

    public List<Task> list;
    Activity activity;
    TextView taskText;
    TextView dateText;

    public ListViewAdapter(Activity activity, List<Task> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.column, null);
            taskText = (TextView) convertView.findViewById(R.id.txtTodoTitle);
            dateText = (TextView) convertView.findViewById(R.id.txtTodoDueDate);
        }

        // Setting the TextView(s) text based on the map
        Task task = list.get(position);
        taskText.setText(task.getTask());

        long date = task.getDueDate();
        if (date==NO_DATE_IDENTIFIER) {
            dateText.setText(NO_DATE_STR);
        }
        else {
            Date dueDate = new Date(task.getDueDate());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            dateText.setText(df.format(dueDate));

            // Comparing the current date and due date, to define the color of the task
            Date currentDate = getDate();

            if (currentDate.after(dueDate)) {
                taskText.setTextColor(Color.RED);
                dateText.setTextColor(Color.RED);
            } else {
                taskText.setTextColor(Color.BLACK);
                dateText.setTextColor(Color.BLACK);
            }
        }

        return convertView;
    }

    /**
     * A getter for midnight of this day
     *
     * @return Today's date, with the clock set to 00:00:00:0
     */
    private Date getDate() {
        Calendar cal = new GregorianCalendar();
        // Reset hour, minutes, seconds and millis
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
}