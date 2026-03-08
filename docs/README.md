# KingSIM User Guide

KingSIM is a simple command-line chatbot that helps users manage todos, deadlines, and events quickly through text commands.

## Features

**Notes about the command format**

* Words in `UPPER_CASE` are parameters to be supplied by the user.  
  e.g. `todo DESCRIPTION`

* Parameters for each command must follow the exact format shown.

* Date and time must be entered in the format `yyyy-MM-dd HHmm`.  
  e.g. `2026-03-10 1800`

## Viewing all tasks: `list`

Shows all tasks currently stored in KingSIM.

**Format:** `list`

## Adding a todo: `todo`

Adds a simple task.

**Format:** `todo DESCRIPTION`

**Example:** `todo buy groceries`

## Adding a deadline: `deadline`

Adds a task with a deadline.

**Format:** `deadline DESCRIPTION /by DATE_TIME`

**Example:** `deadline submit report /by 2026-03-10 1800`

## Adding an event: `event`

Adds a task that takes place within a time period.

**Format:** `event DESCRIPTION /from START_TIME /to END_TIME`

**Example:** `event meeting /from 2026-03-10 1400 /to 2026-03-10 1600`

> [IMPORTANT]
> The end time must not be earlier than the start time.

## Marking a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark INDEX`

**Example:** `mark 2`

## Unmarking a task: `unmark`

Marks the specified task as not completed.

**Format:** `unmark INDEX`

**Example:** `unmark 2`

## Deleting a task: `delete`

Deletes the specified task.

**Format:** `delete INDEX`

**Example:** `delete 3`

## Finding tasks: `find`

Finds tasks containing the given keyword.

**Format:** `find KEYWORD`

**Example:** `find book`

The search is case-insensitive.

## Exiting the program: `bye`

Exits KingSIM.

**Format:** `bye`

## Saving the data

KingSIM saves tasks automatically after every command that changes the task list. There is no need to save manually.

## Command summary

| Action | Format |
| --- | --- |
| List tasks | `list` |
| Add todo | `todo DESCRIPTION` |
| Add deadline | `deadline DESCRIPTION /by DATE_TIME` |
| Add event | `event DESCRIPTION /from START_TIME /to END_TIME` |
| Mark task | `mark INDEX` |
| Unmark task | `unmark INDEX` |
| Delete task | `delete INDEX` |
| Find tasks | `find KEYWORD` |
| Exit | `bye` |