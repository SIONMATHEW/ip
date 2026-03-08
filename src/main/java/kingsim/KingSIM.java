package kingsim;

import java.io.IOException;

/**
 * Represents the main KingSIM chatbot application.
 */
public class KingSIM {

    private final Storage storage;
    private final Ui ui;
    private final TaskList tasks;

    /**
     * Creates a KingSIM chatbot using the given file path for storage.
     *
     * @param filePath Path to the data file.
     */
    public KingSIM(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot until the user exits.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            try {
                if (input.isEmpty()) {
                    throw KingSimException.emptyInput();
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

                if (lower.equals("find") || lower.startsWith("find ")) {
                    handleFind(input);
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
                ui.showError("Something unexpected happened. KingSIM needs a second to recover.");
            }
        }

        ui.close();
    }

    /**
     * Starts the chatbot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new KingSIM("./data/kingsim.txt").run();
    }

    /**
     * Saves the current task list to storage.
     *
     * @throws KingSimException If saving to the data file fails.
     */

    private void saveQuietly() throws KingSimException {
        try {
            storage.save(tasks.getAll());
        } catch (IOException e) {
            throw KingSimException.saveFailed();
        }
    }

    private void handleFind(String input) throws KingSimException {
        String keyword = input.length() > 4 ? input.substring(4).trim() : "";

        if (keyword.isEmpty()) {
            throw KingSimException.emptyFindKeyword();
        }

        ui.showFindResults(tasks.find(keyword));
    }

    /**
     * Marks or unmarks the task specified in the user input.
     *
     * @param input Full user input for the mark or unmark command.
     * @param isMark True to mark the task as done, false to unmark it.
     * @throws KingSimException If the task number is missing or invalid.
     */
    private void handleMarkUnmark(String input, boolean isMark) throws KingSimException {
        String command = isMark ? "mark" : "unmark";
        int index = Parser.parseTaskIndex(input, command);

        if (index < 0 || index >= tasks.size()) {
            throw KingSimException.invalidTaskNumber();
        }

        if (isMark) {
            tasks.get(index).markDone();
            ui.showMarkMessage("Nice! I've marked this task as done:", tasks.get(index));
        } else {
            tasks.get(index).unmarkDone();
            ui.showMarkMessage("Alright, I've marked this task as not done yet:", tasks.get(index));
        }
    }
    /**
     * Deletes the task specified in the user input.
     *
     * @param input Full user input for the delete command.
     * @throws KingSimException If the task number is missing or invalid.
     */

    private void handleDelete(String input) throws KingSimException {
        String command = "delete";
        int index = Parser.parseTaskIndex(input, command);

        if (index < 0 || index >= tasks.size()) {
            throw KingSimException.invalidTaskNumber();
        }

        Task removed = tasks.remove(index);
        ui.showDeleteMessage(removed, tasks.size());
    }
}