package kingsim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user input into tasks or command details.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

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
                throw new Exception("A deadline needs details. Try: deadline return book /by 2026-03-10 1800");
            }

            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw new Exception("Missing /by. Format: deadline <task> /by yyyy-MM-dd HHmm");
            }

            String desc = parts[0].trim();
            String byText = parts[1].trim();

            if (desc.isEmpty()) {
                throw new Exception("Deadline description can't be empty.");
            }
            if (byText.isEmpty()) {
                throw new Exception("Please add the due date after /by.");
            }

            try {
                LocalDateTime by = LocalDateTime.parse(byText, INPUT_FORMAT);
                return new Deadline(desc, by);
            } catch (DateTimeParseException e) {
                throw new Exception("Use date and time format yyyy-MM-dd HHmm, e.g. 2026-03-10 1800");
            }
        }

        if (lower.equals("event") || lower.startsWith("event ")) {
            String rest = input.length() > 5 ? input.substring(5).trim() : "";
            if (rest.isEmpty()) {
                throw new Exception("An event needs details. Try: event meeting /from 2026-03-10 1400 /to 2026-03-10 1600");
            }

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos + 7) {
                throw new Exception("Format: event <name> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
            }

            String desc = rest.substring(0, fromPos).trim();
            String fromText = rest.substring(fromPos + 7, toPos).trim();
            String toText = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw new Exception("Event name can't be empty.");
            }
            if (fromText.isEmpty()) {
                throw new Exception("Please include a start date and time after /from.");
            }
            if (toText.isEmpty()) {
                throw new Exception("Please include an end date and time after /to.");
            }

            try {
                LocalDateTime from = LocalDateTime.parse(fromText, INPUT_FORMAT);
                LocalDateTime to = LocalDateTime.parse(toText, INPUT_FORMAT);

                if (to.isBefore(from)) {
                    throw new Exception("Event end time cannot be before start time.");
                }

                return new Event(desc, from, to);
            } catch (DateTimeParseException e) {
                throw new Exception("Use date and time format yyyy-MM-dd HHmm, e.g. 2026-03-10 1400");
            }
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