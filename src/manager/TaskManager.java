package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    void createTasks(Task task);

    void updateTasks(Task task);

    Task getTasksById(int id);

    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    void deleteTasksById(int id);

    void createEpics(Epic epic);

    void updateEpics(Epic epic);

    Epic getEpicsById(int id);

    ArrayList<Subtask> getSubTaskFromEpic(int id);

    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    void deleteEpicsById(int id);

    void createSubTasks(Subtask subtask);

    void updateSubTasks(Subtask subtask);

    Subtask getSubTasksById(int id);

    ArrayList<Subtask> getAllSubTasks();

    void deleteAllSubTasks();

    void deleteSubTasksById(int id);

    void checkEpicStatus(Epic epic);
}
