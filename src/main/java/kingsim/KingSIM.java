package kingsim;

import java.io.IOException;

/**
 * KingSIM is a simple chatbot that can add tasks, list tasks, and mark/unmark tasks as done.
 */
public class KingSIM {

    private final Storage storage;
    private final Ui ui;
    private final TaskList tasks;

    // Custom exception for input errors
    private static class KingSimException extends Exception {
        public KingSimException(String message) {
            super(message);
        }
    }

    public KingSIM(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
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
                    ui.showList(tasks.getAll());
                    continue;
                }

                if (lower.equals("mark") || lower.startsWith("mark ")) {
                    handleMarkUnmark(input, true);
                    saveQuietly();
                    continue;
                }

                if (lower.equals("unmark") || lower.startsWith("unmark ")) {
                    handleMarkUnmark(input, false);
                    saveQuietly();
                    continue;
                }

                if (lower.equals("delete") || lower.startsWith("delete ")) {
                    handleDelete(input);
                    saveQuietly();
                    continue;
                }

                Task newTask = Parser.parseTask(input);
                tasks.add(newTask);
                ui.showAddTask(newTask, tasks.size());
                saveQuietly();

            } catch (KingSimException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new KingSIM("./data/kingsim.txt").run();
    }

    private void saveQuietly() throws KingSimException {
        try {
            storage.save(tasks.getAll());
        } catch (IOException e) {
            throw new KingSimException("I couldn't save your tasks to disk.");
        }
    }

    private void handleMarkUnmark(String input, boolean isMark) throws Exception {
        String command = isMark ? "mark" : "unmark";
        int index = Parser.parseTaskIndex(input, command);

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

    private void handleDelete(String input) throws Exception {
        String command = "delete";
        int index = Parser.parseTaskIndex(input, command);

        if (index < 0 || index >= tasks.size()) {
            throw new KingSimException("That task number doesn’t exist yet.");
        }

        Task removed = tasks.remove(index);
        ui.showDeleteMessage(removed, tasks.size());
    }
}