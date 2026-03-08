package kingsim;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a UI object for reading user input and showing output.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return User command after trimming surrounding whitespace.
     */
    public String readCommand() {
        String input = scanner.nextLine().trim();

        if (input.contains(LINE)) {
            input = input.substring(0, input.indexOf(LINE)).trim();
        }

        return input;
    }

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Shows the goodbye message.
     */
    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Shows an error message.
     *
     * @param message Message to be shown.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Shows the message after adding a task.
     *
     * @param task Task that was added.
     * @param taskCount Current number of tasks in the list.
     */
    public void showAddTask(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    /**
     * Shows all tasks in the list.
     *
     * @param tasks Tasks to be shown.
     */
    public void showList(ArrayList<Task> tasks) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("Your list is empty for now.");
            System.out.println("Add one with: todo <task>");
            System.out.println(LINE);
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Shows the list of matching tasks found by a keyword search.
     *
     * @param tasks Matching tasks to be shown.
     */
    public void showFindResults(ArrayList<Task> tasks) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found.");
            System.out.println(LINE);
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Shows the message after marking or unmarking a task.
     *
     * @param message Message to be shown.
     * @param task Task affected.
     */
    public void showMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Shows the message after deleting a task.
     *
     * @param task Task that was removed.
     * @param taskCount Current number of tasks in the list.
     */
    public void showDeleteMessage(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Closes the scanner used for reading input.
     */
    public void close() {
        scanner.close();
    }
}