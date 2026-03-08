package kingsim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy h:mm a");

    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}