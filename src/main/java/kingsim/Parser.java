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

    /**
     * Parses user input and returns the corresponding task.
     *
     * @param input User input entered in the console.
     * @return Task created from the input.
     * @throws KingSimException If the input format is invalid.
     */
    public static Task parseTask(String input) throws KingSimException {
        String lower = input.toLowerCase();

        if (lower.equals("todo") || lower.startsWith("todo ")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";
            if (desc.isEmpty()) {
                throw KingSimException.emptyTodo();
            }
            return new Todo(desc);
        }

        if (lower.equals("deadline") || lower.startsWith("deadline ")) {
            String rest = input.length() > 8 ? input.substring(8).trim() : "";
            if (rest.isEmpty()) {
                throw KingSimException.incompleteDeadline();
            }

            String[] parts = rest.split(" /by ", 2);
            if (parts.length < 2) {
                throw KingSimException.missingBy();
            }

            String desc = parts[0].trim();
            String byText = parts[1].trim();

            if (desc.isEmpty()) {
                throw KingSimException.emptyDeadlineDescription();
            }
            if (byText.isEmpty()) {
                throw KingSimException.emptyDeadlineDate();
            }

            try {
                LocalDateTime by = LocalDateTime.parse(byText, INPUT_FORMAT);
                return new Deadline(desc, by);
            } catch (DateTimeParseException e) {
                throw KingSimException.invalidDateTime("2026-03-10 1800");
            }
        }

        if (lower.equals("event") || lower.startsWith("event ")) {
            String rest = input.length() > 5 ? input.substring(5).trim() : "";
            if (rest.isEmpty()) {
                throw KingSimException.incompleteEvent();
            }

            int fromPos = rest.indexOf(" /from ");
            int toPos = rest.indexOf(" /to ");

            if (fromPos == -1 || toPos == -1 || toPos < fromPos + 7) {
                throw KingSimException.invalidEventFormat();
            }

            String desc = rest.substring(0, fromPos).trim();
            String fromText = rest.substring(fromPos + 7, toPos).trim();
            String toText = rest.substring(toPos + 5).trim();

            if (desc.isEmpty()) {
                throw KingSimException.emptyEventName();
            }
            if (fromText.isEmpty()) {
                throw KingSimException.emptyEventStart();
            }
            if (toText.isEmpty()) {
                throw KingSimException.emptyEventEnd();
            }

            try {
                LocalDateTime from = LocalDateTime.parse(fromText, INPUT_FORMAT);
                LocalDateTime to = LocalDateTime.parse(toText, INPUT_FORMAT);

                if (to.isBefore(from)) {
                    throw KingSimException.invalidEventRange();
                }

                return new Event(desc, from, to);
            } catch (DateTimeParseException e) {
                throw KingSimException.invalidDateTime("2026-03-10 1400");
            }
        }

        throw KingSimException.unknownCommand();
    }

    /**
     * Parses the task index from a command such as mark, unmark, or delete.
     *
     * @param input Full user input.
     * @param command Command name at the start of the input.
     * @return Zero-based task index.
     * @throws KingSimException If the index is missing or invalid.
     */
    public static int parseTaskIndex(String input, String command) throws KingSimException {
        String numberPart = input.substring(command.length()).trim();

        if (numberPart.isEmpty()) {
            throw KingSimException.missingNumber(command);
        }

        try {
            return Integer.parseInt(numberPart) - 1;
        } catch (NumberFormatException e) {
            throw KingSimException.invalidNumber(command);
        }
    }
}