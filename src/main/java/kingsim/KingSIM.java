package kingsim;

import java.util.Scanner;

/**
 * kingsim.KingSIM is a simple chatbot that can add tasks, list tasks,
 * and mark/unmark tasks as done.
 */
public class KingSIM {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    // Custom exception for input errors
    private static class KingSimException extends Exception {
        public KingSimException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        printWelcome();

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

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
                    printList(tasks, taskCount);
                    continue;
                }

                if (lower.equals("mark") || lower.startsWith("mark ")) {
                    handleMarkUnmark(tasks, taskCount, input, true);
                    continue;
                }

                if (lower.equals("unmark") || lower.startsWith("unmark ")) {
                    handleMarkUnmark(tasks, taskCount, input, false);
                    continue;
                }

                if (taskCount >= MAX_TASKS) {
                    throw new KingSimException("I’m full. I can only store up to " + MAX_TASKS + " tasks.");
                }

                Task newTask = parseTask(input);
                tasks[taskCount] = newTask;
                taskCount++;

                printAddTaskMessage(newTask, taskCount);

            } catch (KingSimException e) {
                printError(e.getMessage());
            } catch (Exception e) {
                // Safety net: app should never crash due to unexpected input
                printError("Oops—something went wrong. Try again?");
            }
        }

        scanner.close();
    }

    private static void printWelcome() {
        System.out.println(LINE);
        System.out.println("Hello! I'm kingsim.KingSIM");
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

    private static void printList(Task[] tasks, int taskCount) {
        System.out.println(LINE);

        if (taskCount == 0) {
            System.out.println("Your list is empty for now.");
            System.out.println("Add one with: todo <task>");
            System.out.println(LINE);
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void handleMarkUnmark(Task[] tasks, int taskCount, String input, boolean isMark)
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

        if (index < 0 || index >= taskCount) {
            throw new KingSimException("That task number doesn’t exist yet.");
        }

        if (isMark) {
            tasks[index].markDone();
            printMarkMessage("Nice! I've marked this task as done:", tasks[index]);
        } else {
            tasks[index].unmarkDone();
            printMarkMessage("Alright, I've marked this task as not done yet:", tasks[index]);
        }
    }

    private static void printMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Parses user input into a kingsim.Task (kingsim.Todo/kingsim.Deadline/kingsim.Event).
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
                throw new KingSimException("kingsim.Deadline description can't be empty.");
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

            // Minimal safety guard to prevent unexpected substring errors (e.g., "/from /to ...")
            if (fromPos == -1 || toPos == -1 || toPos < fromPos + 7) {
                throw new KingSimException("Format: event <name> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 7, toPos).trim();
            String to = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new KingSimException("kingsim.Event name can't be empty.");
            }
            if (from.isEmpty()) {
                throw new KingSimException("Please include a start time after /from.");
            }
            if (to.isEmpty()) {
                throw new KingSimException("Please include an end time after /to.");
            }

            return new Event(desc, from, to);
        }

        throw new KingSimException("Sorry, I don’t know that command. Try: list, todo, deadline, event, mark, unmark, bye");
    }
}
