package il.ac.huji.todolist;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static il.ac.huji.todolist.Constants.FIRST_COLUMN;
import static il.ac.huji.todolist.Constants.NO_DATE_STR;
import static il.ac.huji.todolist.Constants.SECOND_COLUMN;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView task;
    TextView date;

    public ListViewAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
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
            task = (TextView) convertView.findViewById(R.id.txtTodoTitle);
            date = (TextView) convertView.findViewById(R.id.txtTodoDueDate);
        }

        // Setting the TextView(s) text based on the map
        HashMap<String, String> map = list.get(position);
        task.setText(map.get(FIRST_COLUMN));
        date.setText(map.get(SECOND_COLUMN));

        String dateString = map.get(SECOND_COLUMN);

        if (dateString.equals(NO_DATE_STR)) {
            return convertView;
        }

        // Comparing the current date and due date, to define the color of the task
        Date currentDate = getDate();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dueDate = null;
        try {
            dueDate = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (currentDate.after(dueDate)) {
            task.setTextColor(Color.RED);
            date.setTextColor(Color.RED);
        } else {
            task.setTextColor(Color.BLACK);
            date.setTextColor(Color.BLACK);
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