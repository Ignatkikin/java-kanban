package manager;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    public static final String HEAD = "id,type,name,status,desctiption,starttime,duration,endtime,epic\n";
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(HEAD);

            for (Task task : tasks.values()) {
                writer.write(toString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                writer.write(toString(epic) + "\n");
            }

            for (Subtask subtask : subTasks.values()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        int maxId = 0;
        FileBackedTaskManager loader = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                Task task = fromString(line);
                int taskId = task.getId();
                if (taskId > maxId) {
                    maxId = taskId;
                }

                switch (task.getType()) {
                    case TASK:
                        loader.tasks.put(task.getId(), task);
                        if (task.getStartTime() != null) {
                            loader.addPrioritizedTask(task);
                        }
                        break;
                    case EPIC:
                        loader.epics.put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        loader.subTasks.put(task.getId(), (Subtask) task);
                        loader.epics.get(((Subtask) task).getEpicId()).addSubtasksId(task.getId());
                        if (task.getStartTime() != null) {
                            loader.addPrioritizedTask(task);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении их файла");
        }
        loader.counter = maxId;
        return loader;
    }


    public static Task fromString(String value) {
        String[] taskSplit = value.split(",");

        int id = Integer.parseInt(taskSplit[0]);
        TaskType type = TaskType.valueOf(taskSplit[1]);
        String name = taskSplit[2];
        Status status = Status.valueOf(taskSplit[3]);
        String description = taskSplit[4];
        LocalDateTime startTime = parseDateTime(taskSplit[5]);
        Duration duration = parseDuration(taskSplit[6]);
        LocalDateTime endTime = parseDateTime(taskSplit[7]);
        switch (type) {
            case EPIC:
                return new Epic(id, name, description, status, startTime,
                        duration, endTime);
            case SUBTASK:
                int epicId = Integer.parseInt(taskSplit[8]);
                return new Subtask(id, name, description, status, epicId,
                        startTime, duration);
            default:
                return new Task(id, name, description, status,
                        startTime, duration);
        }
    }

    public static LocalDateTime parseDateTime(String date) {
        if (date.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(date);
    }

    public static Duration parseDuration(String duration) {
        if (duration.equals("null")) {
            return null;
        }
        return Duration.parse(duration);
    }

    public String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId())
                .append(",")
                .append(task.getType())
                .append(",")
                .append(task.getName())
                .append(",")
                .append(task.getStatus())
                .append(",")
                .append(task.getDescription())
                .append(",")
                .append(task.getStartTime())
                .append(",")
                .append(task.getDuration())
                .append(",")
                .append(task.getEndTime());

        if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(",").append(subtask.getEpicId());
        }
        return sb.toString();
    }

    @Override
    public void createTasks(Task task) {
        super.createTasks(task);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTasksById(int id) {
        super.deleteTasksById(id);
        save();
    }

    @Override
    public void createEpics(Epic epic) {
        super.createEpics(epic);
        save();
    }

    @Override
    public void updateEpics(Epic epic) {
        super.updateEpics(epic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicsById(int id) {
        super.deleteEpicsById(id);
        save();
    }

    @Override
    public void createSubTasks(Subtask subtask) {
        super.createSubTasks(subtask);
        save();
    }

    @Override
    public void updateSubTasks(Subtask subtask) {
        super.updateSubTasks(subtask);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteSubTasksById(int id) {
        super.deleteSubTasksById(id);
        save();
    }
}
