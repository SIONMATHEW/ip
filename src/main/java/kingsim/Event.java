package kingsim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task.
 */
public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy h:mm a");

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task with the given description, start time, and end time.
     *
     * @param description Description of the event.
     * @param from Start date and time of the event.
     * @param to End date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    LocalDateTime getFrom() {
        return from;
    }

    LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString()
                + " (from: " + from.format(OUTPUT_FORMAT)
                + " to: " + to.format(OUTPUT_FORMAT) + ")";
    }
}