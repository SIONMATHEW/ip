package kingsim;

/**
 * Represents a task with a description and a done/not-done status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmarkDone() {
        isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns whether this task has been marked as done.
     *
     * @return True if the task is done, false otherwise.
     */
    boolean isDone() {
        return isDone;
    }

    private String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}