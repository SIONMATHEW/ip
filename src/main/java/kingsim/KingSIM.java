package kingsim;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;

/**
 * KingSIM is a simple chatbot that can add tasks, list tasks, and mark/unmark tasks as done.
 */
public class KingSIM {
    private static final String LINE = "____________________________________________________________";

    // Custom exception for input errors
    private static class KingSimException extends Exception {
        public KingSimException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        printWelcome();

        Storage storage = new Storage("./data/kingsim.txt");
        ArrayList<Task> tasks = storage.load();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty()) {
                    throw new KingSimException("No input received. Try: list, todo <task>, deadline <task> /by <when>");
                }

                String lower = input.toLowerCase();

                if (lower.equals("bye")) {
                    printBye();
                    break;
                }

                if (lower.equals("list")) {
                    printList(tasks);
                    continue;
                }

                if (lower.equals("mark") || lower.startsWith("mark ")) {
                    handleMarkUnmark(tasks, input, true);
                    saveQuietly(storage, tasks);
                    continue;
                }

                if (lower.equals("unmark") || lower.startsWith("unmark ")) {
                    handleMarkUnmark(tasks, input, false);
                    saveQuietly(storage, tasks);
                    continue;
                }

                if (lower.equals("delete") || lower.startsWith("delete ")) {
                    handleDelete(tasks, input);
                    saveQuietly(storage, tasks);
                    continue;
                }

                Task newTask = parseTask(input);
                tasks.add(newTask);
                printAddTaskMessage(newTask, tasks.size());
                saveQuietly(storage, tasks);

            } catch (KingSimException e) {
                printError(e.getMessage());
            } catch (Exception e) {
                // Safety net: app should never crash due to unexpected input
                printError("Oops—something went wrong. Try again?");
            }
        }

        scanner.close();
    }

    private static void saveQuietly(Storage storage, ArrayList<Task> tasks) throws KingSimException {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            throw new KingSimException("I couldn't save your tasks to disk.");
        }
    }

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    private static void printAddTaskMessage(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printList(ArrayList<Task> tasks) {
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

    private static void handleMarkUnmark(ArrayList<Task> tasks, String input, boolean isMark)
            throws KingSimException {
        String command = isMark ? "mark" : "unmark";
        String numberPart = input.substring(command.length()).trim();

        if (numberPart.isEmpty()) {
            throw new KingSimException("Which task number? Try: " + command + " 1");
        }

        int index;
        try {
            index = Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            throw new KingSimException("That doesn’t look like a number. Try: " + command + " 1");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new KingSimException("That task number doesn’t exist yet.");
        }

        if (isMark) {
            tasks.get(index).markDone();
            printMarkMessage("Nice! I've marked this task as done:", tasks.get(index));
        } else {
            tasks.get(index).unmarkDone();
            printMarkMessage("Alright, I've marked this task as not done yet:", tasks.get(index));
        }
    }

    private static void handleDelete(ArrayList<Task> tasks, String input) throws KingSimException {
        String command = "delete";
        String numberPart = input.substring(command.length()).trim();

        if (numberPart.isEmpty()) {
            throw new KingSimException("Which task number? Try: delete 1");
        }

        int index;
        try {
            index = Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            throw new KingSimException("That doesn’t look like a number. Try: delete 1");
        }

        if (index < 0 || index >= tasks.size()) {
            throw new KingSimException("That task number doesn’t exist yet.");
        }

        Task removed = tasks.remove(index);

        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Parses user input into a KingSIM Task
     * Throws KingSimException if the input is invalid.
     */
    private static Task parseTask(String input) throws KingSimException {
        String lower = input.toLowerCase();

        if (lower.equals("todo") || lower.startsWith("todo ")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";
            if (desc.isEmpty()) {
                throw new KingSimException("Your todo needs a description. Try: todo buy milk");
            }
            return new Todo(desc);
        }

        if (lower.equals("deadline") || lower.startsWith("deadline ")) {
            String rest = input.length() > 8 ? input.substring(8).trim() : "";
            if (rest.isEmpty()) {
                throw new KingSimException("A deadline needs details. Try: deadline return book /by Sunday");
            }

            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new KingSimException("Missing /by. Format: deadline <task> /by <when>");
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty()) {
                throw new KingSimException("Deadline description can't be empty.");
            }
            if (by.isEmpty()) {
                throw new KingSimException("Please add the due date after /by.");
            }

            return new Deadline(desc, by);
        }

        if (lower.equals("event") || lower.startsWith("event ")) {
            String rest = input.length() > 5 ? input.substring(5).trim() : "";
            if (rest.isEmpty()) {
                throw new KingSimException("An event needs details. Try: event meeting /from 2pm /to 4pm");
            }

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos + 7) {
                throw new KingSimException("Format: event <name> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 7, toPos).trim();
            String to = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new KingSimException("Event name can't be empty.");
            }
            if (from.isEmpty()) {
                throw new KingSimException("Please include a start time after /from.");
            }
            if (to.isEmpty()) {
                throw new KingSimException("Please include an end time after /to.");
            }

            return new Event(desc, from, to);
        }

        throw new KingSimException("Sorry, I don’t know that command. Try: list, todo, deadline, event, mark, unmark, delete, bye");
    }
}
