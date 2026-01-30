import java.util.Scanner;

/**
 * KingSIM is a simple chatbot that can add tasks, list tasks and mark/unmark tasks as done.
 */
public class KingSIM {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;


    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                printBye();
                break;
            }

            if (input.equalsIgnoreCase("list")) {
                printList(tasks, taskCount);
                continue;
            }

            if (input.toLowerCase().startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[index].markDone();
                printMarkMessage("Nice! I've marked this task as done:", tasks[index]);
                continue;
            }

            if (input.toLowerCase().startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[index].unmarkDone();
                printMarkMessage("OK, I've marked this task as not done yet:", tasks[index]);
                continue;
            }

            tasks[taskCount] = new Task(input);
            taskCount++;

            System.out.println(LINE);
            System.out.println("added: " + input);
            System.out.println(LINE);
        }

        scanner.close();
    }

    private static void printBye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
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

    private static void printMarkMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println("  " + task);
        System.out.println(LINE);
    }
}
