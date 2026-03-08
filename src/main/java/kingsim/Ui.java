package kingsim;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles interactions with the user.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        String input = scanner.nextLine().trim();

        if (input.contains(LINE)) {
            input = input.substring(0, input.indexOf(LINE)).trim();
        }

        return input;
    }

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public void showBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    public void showAddTask(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

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

    public void showMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    public void showDeleteMessage(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    public void close() {
        scanner.close();
    }
}