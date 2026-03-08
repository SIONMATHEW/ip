package kingsim;

/**
 * Parses user input into tasks or command details.
 */
public class Parser {

    public static Task parseTask(String input) throws Exception {
        String lower = input.toLowerCase();

        if (lower.equals("todo") || lower.startsWith("todo ")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";
            if (desc.isEmpty()) {
                throw new Exception("Your todo needs a description. Try: todo buy milk");
            }
            return new Todo(desc);
        }

        if (lower.equals("deadline") || lower.startsWith("deadline ")) {
            String rest = input.length() > 8 ? input.substring(8).trim() : "";
            if (rest.isEmpty()) {
                throw new Exception("A deadline needs details. Try: deadline return book /by Sunday");
            }

            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new Exception("Missing /by. Format: deadline <task> /by <when>");
            }

            String desc = parts[0].trim();
            String by = parts[1].trim();

            if (desc.isEmpty()) {
                throw new Exception("Deadline description can't be empty.");
            }
            if (by.isEmpty()) {
                throw new Exception("Please add the due date after /by.");
            }

            return new Deadline(desc, by);
        }

        if (lower.equals("event") || lower.startsWith("event ")) {
            String rest = input.length() > 5 ? input.substring(5).trim() : "";
            if (rest.isEmpty()) {
                throw new Exception("An event needs details. Try: event meeting /from 2pm /to 4pm");
            }

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos + 7) {
                throw new Exception("Format: event <name> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 7, toPos).trim();
            String to = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new Exception("Event name can't be empty.");
            }
            if (from.isEmpty()) {
                throw new Exception("Please include a start time after /from.");
            }
            if (to.isEmpty()) {
                throw new Exception("Please include an end time after /to.");
            }

            return new Event(desc, from, to);
        }

        throw new Exception("Sorry, I don’t know that command. Try: list, todo, deadline, event, mark, unmark, delete, bye");
    }

    public static int parseTaskIndex(String input, String command) throws Exception {
        String numberPart = input.substring(command.length()).trim();

        if (numberPart.isEmpty()) {
            throw new Exception("Which task number? Try: " + command + " 1");
        }

        try {
            return Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            throw new Exception("That doesn’t look like a number. Try: " + command + " 1");
        }
    }
}