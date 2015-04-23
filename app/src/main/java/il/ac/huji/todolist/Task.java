package il.ac.huji.todolist;

/**
 * This class represents a Task instance
 */
public class Task {
    private String task;
    private String dueDate;
    private long id;

    public Task() {
        this.task = null;
        this.dueDate = Constants.NO_DATE_STR;
    }

    public Task (String task, String dueDate) {
        super();
        this.task = task;
        this.dueDate = dueDate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTask() {
        return this.task;
    }

    public void setTask (String task) {
        this.task = task;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public void setDueDate (String dueDate) {
        this.dueDate = dueDate;
    }


}
