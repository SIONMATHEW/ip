import java.util.Scanner;

public class KingSIM {
    public static void main(String[] args) {
        final String LINE = "____________________________________________________________";
        System.out.println(LINE);
        System.out.println("Hello! I'm KingSIM");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
        Scanner ed = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = ed.nextLine();
            String command = input.trim();

            if (command.equalsIgnoreCase("bye")) {
                System.out.println(LINE);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if (command.equalsIgnoreCase("list")) {
                System.out.println(LINE);
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            if (command.toLowerCase().startsWith("mark ")) {
                int index = Integer.parseInt(command.substring(5).trim()) - 1;
                tasks[index].markDone();

                System.out.println(LINE);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[index]);
                System.out.println(LINE);
                continue;
            }

            if (command.toLowerCase().startsWith("unmark ")) {
                int index = Integer.parseInt(command.substring(7).trim()) - 1;
                tasks[index].unmarkDone();

                System.out.println(LINE);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[index]);
                System.out.println(LINE);
                continue;
            }

            tasks[taskCount++] = new Task(command);
            System.out.println(LINE);
            System.out.println("added: " + command);
            System.out.println(LINE);
        }
        ed.close();
    }
}
