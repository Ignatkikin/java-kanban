package manager;

import exceptions.NotFoundException;
import exceptions.OverlapTimeException;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subTasks;

    protected HistoryManager historyManager;
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int counter = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void addPrioritizedTask(Task task) {
        boolean isOverlap = prioritizedTasks.stream()
                .filter(t -> t.getId() != task.getId())
                .anyMatch(t -> (!(task.getStartTime().isAfter(t.getEndTime()) ||
                        (task.getEndTime().isBefore(t.getStartTime())))));
        if (isOverlap) {
            throw new OverlapTimeException("Задачи пересекаются");
        }
        prioritizedTasks.add(task);
    }

    @Override
    public void createTasks(Task task) {
        task.setId(++counter);
        if (task.getStartTime() != null) {
            addPrioritizedTask(task);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTasks(Task task) {
        Task oldTask = tasks.get(task.getId());
        if (oldTask != null) {
            clearPrioritized(oldTask);
            if (task.getStartTime() != null) {
                addPrioritizedTask(task);
            }
            tasks.put(task.getId(), task);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Task getTasksById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException();
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteTasksById(int id) {
        prioritizedTasks.remove(tasks.remove(id));
        historyManager.remove(id);
    }

    @Override
    public void createEpics(Epic epic) {
        epic.setId(++counter);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setDescription(epic.getDescription());
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Epic getEpicsById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException();
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Subtask> getSubTaskFromEpic(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException();
        }
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (int sbId : epic.getSubtasksId()) {
            subTaskList.add(subTasks.get(sbId));
        }
        return subTaskList;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subTasks.values()) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicsById(int id) {
        List<Integer> subTaskIdlist = epics.get(id).getSubtasksId();
        for (int key : subTaskIdlist) {
            prioritizedTasks.remove(subTasks.remove(key));
            historyManager.remove(key);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void createSubTasks(Subtask subtask) {
        subtask.setId(++counter);
        if (subtask.getStartTime() != null) {
            addPrioritizedTask(subtask);
        }
        subTasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtasksId(subtask.getId());
        checkEpicStatus(epics.get(subtask.getEpicId()));
        checkEpicTime(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateSubTasks(Subtask subtask) {
        Subtask oldSubtask = subTasks.get(subtask.getId());
        if (oldSubtask != null) {
            clearPrioritized(oldSubtask);
            if (subtask.getStartTime() != null) {
                addPrioritizedTask(subtask);
            }
            subTasks.put(subtask.getId(), subtask);
            checkEpicStatus(epics.get(subtask.getEpicId()));
            checkEpicTime(epics.get(subtask.getEpicId()));
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Subtask getSubTasksById(int id) {
        Subtask subtask = subTasks.get(id);
        if (subtask == null) {
            throw new NotFoundException();
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epics.get(epic.getId()).getSubtasksId().clear();
            checkEpicStatus(epics.get(epic.getId()));
            checkEpicTime(epic);
        }
        for (Subtask subtask : subTasks.values()) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTasksById(int id) {
        int sbId = subTasks.get(id).getEpicId();
        prioritizedTasks.remove(subTasks.remove(id));
        epics.get(sbId).getSubtasksId().remove((Integer) id);
        checkEpicStatus(epics.get(sbId));
        checkEpicTime(epics.get(sbId));
        historyManager.remove(id);
    }

    @Override
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

    public void checkEpicTime(Epic epic) {
        List<Task> sabTaskList = getPrioritizedTasks().stream()
                .filter(t -> t.getType().equals(TaskType.SUBTASK))
                .filter(t -> ((Subtask) t).getEpicId() == epic.getId())
                .collect(Collectors.toList());

        if (sabTaskList.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        } else {
            LocalDateTime startTime = sabTaskList.get(0).getStartTime();
            LocalDateTime endTime = sabTaskList.get(sabTaskList.size() - 1).getEndTime();
            Duration duration = Duration.between(startTime, endTime);
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(duration);
        }
    }

    public void clearPrioritized(Task task) {
        List<Task> taskList = getPrioritizedTasks();
        if (taskList.contains(task)) {
            prioritizedTasks.remove(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}


