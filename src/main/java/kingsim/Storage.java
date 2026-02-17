package kingsim;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(filePath)) {
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                Task t = decode(line);
                if (t != null) {
                    tasks.add(t);
                }
            }
        } catch (IOException ignored) {
            // If reading fails, start with empty list (do not crash)
        }
        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        ArrayList<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(encode(t));
        }

        Files.write(filePath, lines);
    }

    private String encode(Task t) {
        String done = t.isDone() ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.getDescription();
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + d.getDescription() + " | " + d.getBy();
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return "E | " + done + " | " + e.getDescription() + " | " + e.getFrom() + " | " + e.getTo();
        } else {
            return "T | " + done + " | " + t.getDescription();
        }
    }

    /**
     * Corruption handling: returns null for invalid lines (skip them).
     */
    private Task decode(String line) {
        try {
            String[] parts = line.split("\\s*\\|\\s*");
            if (parts.length < 3) {
                return null;
            }

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String desc = parts[2].trim();

            Task t;
            if (type.equals("T")) {
                t = new Todo(desc);
            } else if (type.equals("D")) {
                if (parts.length < 4) return null;
                t = new Deadline(desc, parts[3].trim());
            } else if (type.equals("E")) {
                if (parts.length < 5) return null;
                t = new Event(desc, parts[3].trim(), parts[4].trim());
            } else {
                return null;
            }

            if (isDone) {
                t.markDone();
            }
            return t;
        } catch (Exception e) {
            return null;
        }
    }
}
