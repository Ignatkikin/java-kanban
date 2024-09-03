package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subTasks;

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
                .anyMatch(t -> task.getStartTime().isBefore(t.getEndTime()) &&
                        task.getEndTime().isAfter(t.getStartTime()));
        if (isOverlap) {
            throw new RuntimeException("Задачи пересекаются");
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
        if (tasks.containsKey(task.getId())) {
            clearPrioritized(oldTask);
            if (task.getStartTime() != null) {
                addPrioritizedTask(task);
            }
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Task getTasksById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            prioritizedTasks.remove(tasks.get(id));
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteTasksById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
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
        }
    }

    @Override
    public Epic getEpicsById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Subtask> getSubTaskFromEpic(int id) {
        Epic epic = epics.get(id);
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
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        for (Integer sbId : subTasks.keySet()) {
            historyManager.remove(sbId);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicsById(int id) {
        ArrayList<Integer> subTaskIdlist = epics.get(id).getSubtasksId();
        for (int key : subTaskIdlist) {
            prioritizedTasks.remove(subTasks.get(key));
            subTasks.remove(key);
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
        if (subTasks.containsKey(subtask.getId())) {
            clearPrioritized(oldSubtask);
            if (subtask.getStartTime() != null) {
                addPrioritizedTask(subtask);
            }
            subTasks.put(subtask.getId(), subtask);
            checkEpicStatus(epics.get(subtask.getEpicId()));
            checkEpicTime(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public Subtask getSubTasksById(int id) {
        Subtask subtask = subTasks.get(id);
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
        for (Integer sbId : subTasks.keySet()) {
            prioritizedTasks.remove(subTasks.get(sbId));
            historyManager.remove(sbId);
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubTasksById(int id) {
        prioritizedTasks.remove(subTasks.get(id));
        int sbId = subTasks.get(id).getEpicId();
        subTasks.remove(id);
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
        List<Integer> subTaskFromEpic = epic.getSubtasksId();

        if (!subTaskFromEpic.isEmpty()) {
            LocalDateTime startTime = subTaskFromEpic.stream()
                    .map(subTasks::get)
                    .filter(subtask -> subtask.getStartTime() != null)
                    .min(Comparator.comparing(Task::getStartTime))
                    .map(Task::getStartTime)
                    .orElse(null);

            LocalDateTime endTime = subTaskFromEpic.stream()
                    .map(subTasks::get)
                    .filter(subtask -> subtask.getStartTime() != null)
                    .max(Comparator.comparing(Task::getEndTime))
                    .map(Task::getEndTime)
                    .orElse(null);

            if (startTime != null && endTime != null) {
                Duration duration = Duration.between(startTime, endTime);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
            }
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    public void clearPrioritized(Task task) {
        List<Task> taskList = getPrioritizedTasks();
        for (Task list : taskList) {
            if (list.getId() == task.getId()) {
                prioritizedTasks.remove(task);
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}


