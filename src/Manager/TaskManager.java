package Manager;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subTasks;

    public static int counter = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    public static int generateCounter() {
        return ++counter;
    }

    public void createTasks(Task task) {
        task.setId(generateCounter());
        tasks.put(task.getId(), task);
    }

    public void updateTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public Task getTasksById(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasklist = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasklist.add(task);
        }
        return tasklist;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteTasksById (int id) {
        tasks.remove(id);
    }

    public void createEpics(Epic epic) {
        epic.setId(generateCounter());
        epics.put(epic.getId(), epic);
    }

    public void updateEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            ArrayList<Integer> subTaskIdList = epics.get(epic.getId()).getSubtasksId();
            for (Integer sbId : subTaskIdList) {
                epic.getSubtasksId().add(sbId);
            }
            epics.put(epic.getId(), epic);
            checkEpicStatus(epic);
        }
    }

    public Epic getEpicsById(int id) {
        return epics.get(id);
    }

    public ArrayList<Subtask> getSubTaskFromEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (int sbId : epic.getSubtasksId()) {
            subTaskList.add(subTasks.get(sbId));
        }
        return subTaskList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(epic);
        }
        return epicList;
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteEpicsById(int id) {
        ArrayList<Integer> subTaskIdlist = epics.get(id).getSubtasksId();
        for (int key : subTaskIdlist) {
            subTasks.remove(key);
        }
        epics.remove(id);
    }

    public void createSubTasks(Subtask subtask) {
        subtask.setId(generateCounter());
        subTasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtasksId(subtask.getId());
        checkEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void updateSubTasks(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            checkEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public Subtask getSubTasksById(int id) {
        return subTasks.get(id);
    }

    public ArrayList<Subtask> getAllSubTasks() {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subTasks.values()) {
            subtaskList.add(subtask);
        }
        return subtaskList;
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epics.get(epic.getId()).getSubtasksId().clear();
            checkEpicStatus(epics.get(epic.getId()));
        }
    }

    public void deleteSubTasksById(int id) {
        int sbId = subTasks.get(id).getEpicId();
        subTasks.remove(id);
        for (int i = 0; i < epics.get(sbId).getSubtasksId().size(); i++) {
            if (epics.get(sbId).getSubtasksId().get(i) == id) {
                epics.get(sbId).getSubtasksId().remove(i);
            }
        }

        checkEpicStatus(epics.get(sbId));
    }

    public void checkEpicStatus(Epic epic) {
        int countNew = 0;
        int countInProgress = 0;
        int countDone = 0;
        if (epic.getSubtasksId().isEmpty()) {
            epics.get(epic.getId()).setStatus(Status.NEW);
            return;
        }
        for (int sbId : epic.getSubtasksId()) {
            Subtask subtask = subTasks.get(sbId);
            if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                countInProgress++;
            } else if (subtask.getStatus() == Status.DONE) {
                countDone++;
            }
        }
        if (countNew >= 0 && countInProgress == 0 && countDone == 0) {
            epics.get(epic.getId()).setStatus(Status.NEW);
        } else if (countDone > 0 && countNew == 0 && countInProgress == 0) {
            epics.get(epic.getId()).setStatus(Status.DONE);
        } else {
            epics.get(epic.getId()).setStatus(Status.IN_PROGRESS);
        }
    }

}
