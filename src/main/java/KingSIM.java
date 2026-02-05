import java.util.Scanner;

/**
 * KingSIM is a simple chatbot that can add tasks, list tasks,
 * and mark/unmark tasks as done.
 */
public class KingSIM {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        printWelcome();

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();
            String lower = input.toLowerCase();

            if (lower.equals("bye")) {
                printBye();
                break;
            }

            if (lower.equals("list")) {
                printList(tasks, taskCount);
                continue;
            }

            if (lower.startsWith("mark ")) {
                handleMarkUnmark(tasks, taskCount, input, true);
                continue;
            }

            if (lower.startsWith("unmark ")) {
                handleMarkUnmark(tasks, taskCount, input, false);
                continue;
            }

            if (taskCount >= MAX_TASKS) {
                printError("Sorry, I can only store up to " + MAX_TASKS + " tasks.");
                continue;
            }

            Task newTask = parseTask(input);
            if (newTask == null) {
                // parseTask already printed an error
                continue;
            }

            tasks[taskCount] = newTask;
            taskCount++;

            printAddTaskMessage(newTask, taskCount);
        }

        scanner.close();
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

    private static void printList(Task[] tasks, int taskCount) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void handleMarkUnmark(Task[] tasks, int taskCount, String input, boolean isMark) {
        String command = isMark ? "mark" : "unmark";
        String numberPart = input.substring(command.length()).trim();

        int index;
        try {
            index = Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            printError("Please provide a valid task number. Example: " + command + " 1");
            return;
        }

        if (index < 0 || index >= taskCount) {
            printError("That task number is out of range.");
            return;
        }

        if (isMark) {
            tasks[index].markDone();
            printMarkMessage("Nice! I've marked this task as done:", tasks[index]);
        } else {
            tasks[index].unmarkDone();
            printMarkMessage("OK, I've marked this task as not done yet:", tasks[index]);
        }
    }

    private static void printMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Parses user input into a Task (Todo/Deadline/Event).
     * Returns null if the input is invalid (and prints an error).
     */
    private static Task parseTask(String input) {
        String lower = input.toLowerCase();

        if (lower.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                printError("The description of a todo cannot be empty.");
                return null;
            }
            return new Todo(desc);
        }

        if (lower.startsWith("deadline ")) {
            String rest = input.substring(9).trim();
            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                printError("Usage: deadline <description> /by <by>");
                return null;
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty()) {
                printError("The description of a deadline cannot be empty.");
                return null;
            }
            if (by.isEmpty()) {
                printError("The /by part of a deadline cannot be empty.");
                return null;
            }

            return new Deadline(desc, by);
        }

        if (lower.startsWith("event ")) {
            String rest = input.substring(6).trim();

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                printError("Usage: event <description> /from <from> /to <to>");
                return null;
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 7, toPos).trim(); // 7 = len(" /from ")
            String to = rest.substring(toPos + 5).trim();            // 5 = len(" /to ")

            if (desc.isEmpty()) {
                printError("The description of an event cannot be empty.");
                return null;
            }
            if (from.isEmpty() || to.isEmpty()) {
                printError("The /from and /to parts of an event cannot be empty.");
                return null;
            }

            return new Event(desc, from, to);
        }

        printError("I don't understand that command yet.");
        return null;
    }
}
