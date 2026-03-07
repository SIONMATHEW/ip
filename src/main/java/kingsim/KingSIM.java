package kingsim;

import java.util.ArrayList;
import java.io.IOException;

/**
 * KingSIM is a simple chatbot that can add tasks, list tasks, and mark/unmark tasks as done.
 */
public class KingSIM {

    private final Storage storage;
    private final Ui ui;
    private final ArrayList<Task> tasks;

    // Custom exception for input errors
    private static class KingSimException extends Exception {
        public KingSimException(String message) {
            super(message);
        }
    }

    public KingSIM(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = storage.load();
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            try {
                if (input.isEmpty()) {
                    throw new KingSimException("No input received. Try: list, todo <task>, deadline <task> /by <when>");
                }

                String lower = input.toLowerCase();

                if (lower.equals("bye")) {
                    ui.showBye();
                    break;
                }

                if (lower.equals("list")) {
                    ui.showList(tasks);
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
                ui.showAddTask(newTask, tasks.size());
                saveQuietly(storage, tasks);

            } catch (KingSimException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Oops—something went wrong. Try again?");
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new KingSIM("./data/kingsim.txt").run();
    }

    private static void saveQuietly(Storage storage, ArrayList<Task> tasks) throws KingSimException {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            throw new KingSimException("I couldn't save your tasks to disk.");
        }
    }

    private void handleMarkUnmark(ArrayList<Task> tasks, String input, boolean isMark)
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
            ui.showMarkMessage("Nice! I've marked this task as done:", tasks.get(index));
        } else {
            tasks.get(index).unmarkDone();
            ui.showMarkMessage("Alright, I've marked this task as not done yet:", tasks.get(index));
        }
    }

    private void handleDelete(ArrayList<Task> tasks, String input) throws KingSimException {
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
        ui.showDeleteMessage(removed, tasks.size());
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