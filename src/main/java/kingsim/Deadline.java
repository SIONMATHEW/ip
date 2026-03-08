package kingsim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy h:mm a");

    private final LocalDateTime by;

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description Description of the deadline task.
     * @param by Deadline date and time.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    /**
     * Returns the deadline date and time of this task.
     *
     * @return Deadline date and time.
     */
    LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}