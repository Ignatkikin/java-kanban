package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    void createTasks(Task task);

    void updateTasks(Task task);

    Task getTasksById(int id);

    List<Task> getAllTasks();

    void deleteAllTasks();

    void deleteTasksById(int id);

    void createEpics(Epic epic);

    void updateEpics(Epic epic);

    Epic getEpicsById(int id);

    List<Subtask> getSubTaskFromEpic(int id);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    void deleteEpicsById(int id);

    void createSubTasks(Subtask subtask);

    void updateSubTasks(Subtask subtask);

    Subtask getSubTasksById(int id);

    List<Subtask> getAllSubTasks();

    void deleteAllSubTasks();

    void deleteSubTasksById(int id);

    void checkEpicStatus(Epic epic);
}
