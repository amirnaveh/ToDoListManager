package il.ac.huji.todolist;

/**
 * This class represents a Task instance
 */
public class Task {
    private String task;
    private long dueDate;
    private long id;

    public Task() {
        this.task = null;
        this.dueDate = Constants.NO_DATE_IDENTIFIER;
    }

    public Task (String task, long dueDate) {
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

    public long getDueDate() {
        return this.dueDate;
    }

    public void setDueDate (long dueDate) {
        this.dueDate = dueDate;
    }


}
