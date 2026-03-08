package kingsim;

/**
 * Represents an exception specific to the KingSIM application.
 */
public class KingSimException extends Exception {

    /**
     * Creates a KingSimException with the given message.
     *
     * @param message Error message to show to the user.
     */
    public KingSimException(String message) {
        super(message);
    }

    /**
     * Returns an exception for blank input.
     *
     * @return Exception for empty user input.
     */
    public static KingSimException emptyInput() {
        return new KingSimException(
                "No input received. Try: list, todo <task>, deadline <task> /by <when>");
    }

    /**
     * Returns an exception for missing find keyword.
     *
     * @return Exception for empty find command.
     */
    public static KingSimException emptyFindKeyword() {
        return new KingSimException(
                "Please provide a keyword to search. Try: find book");
    }

    /**
     * Returns an exception for invalid task number.
     *
     * @return Exception for invalid or out-of-range task number.
     */
    public static KingSimException invalidTaskNumber() {
        return new KingSimException("That task number doesn’t exist yet.");
    }

    /**
     * Returns an exception for invalid numeric input after a command.
     *
     * @param command The command expecting a number.
     * @return Exception for non-numeric task number.
     */
    public static KingSimException invalidNumber(String command) {
        return new KingSimException(
                "That doesn’t look like a number. Try: " + command + " 1");
    }

    /**
     * Returns an exception for missing numeric input after a command.
     *
     * @param command The command expecting a number.
     * @return Exception for missing task number.
     */
    public static KingSimException missingNumber(String command) {
        return new KingSimException(
                "Which task number? Try: " + command + " 1");
    }

    /**
     * Returns an exception for missing todo description.
     *
     * @return Exception for blank todo.
     */
    public static KingSimException emptyTodo() {
        return new KingSimException(
                "Your todo needs a description. Try: todo buy milk");
    }

    /**
     * Returns an exception for incomplete deadline command.
     *
     * @return Exception for blank deadline.
     */
    public static KingSimException incompleteDeadline() {
        return new KingSimException(
                "A deadline needs details. Try: deadline return book /by 2026-03-10 1800");
    }

    /**
     * Returns an exception for missing /by in deadline command.
     *
     * @return Exception for bad deadline format.
     */
    public static KingSimException missingBy() {
        return new KingSimException(
                "Missing /by. Format: deadline <task> /by yyyy-MM-dd HHmm");
    }

    /**
     * Returns an exception for blank deadline description.
     *
     * @return Exception for blank deadline description.
     */
    public static KingSimException emptyDeadlineDescription() {
        return new KingSimException("Deadline description can't be empty.");
    }

    /**
     * Returns an exception for blank deadline date.
     *
     * @return Exception for missing deadline date.
     */
    public static KingSimException emptyDeadlineDate() {
        return new KingSimException("Please add the due date after /by.");
    }

    /**
     * Returns an exception for incomplete event command.
     *
     * @return Exception for blank event.
     */
    public static KingSimException incompleteEvent() {
        return new KingSimException(
                "An event needs details. Try: event meeting /from 2026-03-10 1400 /to 2026-03-10 1600");
    }

    /**
     * Returns an exception for invalid event format.
     *
     * @return Exception for bad event format.
     */
    public static KingSimException invalidEventFormat() {
        return new KingSimException(
                "Format: event <name> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
    }

    /**
     * Returns an exception for blank event name.
     *
     * @return Exception for blank event description.
     */
    public static KingSimException emptyEventName() {
        return new KingSimException("Event name can't be empty.");
    }

    /**
     * Returns an exception for missing event start time.
     *
     * @return Exception for missing /from value.
     */
    public static KingSimException emptyEventStart() {
        return new KingSimException(
                "Please include a start date and time after /from.");
    }

    /**
     * Returns an exception for missing event end time.
     *
     * @return Exception for missing /to value.
     */
    public static KingSimException emptyEventEnd() {
        return new KingSimException(
                "Please include an end date and time after /to.");
    }

    /**
     * Returns an exception for invalid event time range.
     *
     * @return Exception for event end before start.
     */
    public static KingSimException invalidEventRange() {
        return new KingSimException(
                "Event end time cannot be before start time. I can manage tasks, not time travel.");
    }

    /**
     * Returns an exception for invalid date-time format.
     *
     * @param example Example date-time to show.
     * @return Exception for bad date-time format.
     */
    public static KingSimException invalidDateTime(String example) {
        return new KingSimException(
                "Use date and time format yyyy-MM-dd HHmm, e.g. " + example);
    }

    /**
     * Returns an exception for unknown command.
     *
     * @return Exception for unsupported command.
     */
    public static KingSimException unknownCommand() {
        return new KingSimException(
                "Sorry, I don’t know that command. Try: list, todo, deadline, event, mark, unmark, delete, bye");
    }

    /**
     * Returns an exception for save failures.
     *
     * @return Exception for storage save issues.
     */
    public static KingSimException saveFailed() {
        return new KingSimException("I couldn't save your tasks to disk.");
    }
}