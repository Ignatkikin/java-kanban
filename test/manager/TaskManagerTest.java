package manager;

import exceptions.OverlapTimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;


    void creatingAndAddingTaskSubTaskEpic() {
        Task task1 = new Task("Задача 1", "Выучить теорию 6 спринта");
        Task task2 = new Task("Задача 2", "Сдать тз6");
        taskManager.createTasks(task1);
        taskManager.createTasks(task2);

        Epic epic1 = new Epic("Epic 1", "Починить авто");
        taskManager.createEpics(epic1);
        int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("SubTask 2", "Починить дверь", Status.NEW, idEpic1);
        Subtask subtask3 = new Subtask("SubTask 3", "Поменять лампочки", Status.NEW, idEpic1);
        taskManager.createSubTasks(subtask1);
        taskManager.createSubTasks(subtask2);
        taskManager.createSubTasks(subtask3);

        Epic epic2 = new Epic("Epic 2", "Съездить на море");
        taskManager.createEpics(epic2);
        int idEpic2 = epic2.getId();
        Subtask subtask2_1 = new Subtask("SubTask 2_1", "Купить билеты", Status.NEW, idEpic2);
        Subtask subtask2_2 = new Subtask("SubTask 2_2", "Собрать чемодан", Status.NEW, idEpic2);
        Subtask subtask2_3 = new Subtask("SubTask 2_3", "Попросить друга следить за котом",
                Status.NEW, idEpic2);
        taskManager.createSubTasks(subtask2_1);
        taskManager.createSubTasks(subtask2_2);
        taskManager.createSubTasks(subtask2_3);
    }

    @Test
    public void cheakAddAllTasks() {
        creatingAndAddingTaskSubTaskEpic();
        assertFalse(taskManager.getAllTasks().isEmpty(), "Ошибка добавления задач");
        assertFalse(taskManager.getAllEpics().isEmpty(), "Ошибка добавления Эпиков");
        assertFalse(taskManager.getAllSubTasks().isEmpty(), "Ошибка добавления Сабтаск");
    }

    @Test
    public void fieldsMustBeEqualBeforeAndAfterAddingToTheManager() {
        creatingAndAddingTaskSubTaskEpic();
        Task testTask = new Task("Задача 3", "Описание");
        String name = testTask.getName();
        String description = testTask.getDescription();
        Status status = testTask.getStatus();
        taskManager.createTasks(testTask);
        int testTaskid = testTask.getId();

        assertEquals(taskManager.getTasksById(testTaskid).getName(), name,
                "Ошибка с совподением имени до и после добавления в manager");
        assertEquals(taskManager.getTasksById(testTaskid).getDescription(), description,
                "Ошибка с совподением описания до и после добавления в manager");
        assertEquals(taskManager.getTasksById(testTaskid).getStatus(), status,
                "Ошибка с совподением статуса до и после добавления в manager");
    }

    @Test
    public void checkUpdateTasksEpicSubTask() {
        creatingAndAddingTaskSubTaskEpic();
        Task updateTask = new Task("Обновленная задача", "Обновленное описание",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofHours(1));
        updateTask.setId(1);
        Epic updateEpic = new Epic("Обновленый Эпик", "Подзадача Эпика");
        updateEpic.setId(3);
        Subtask updateSubtask = new Subtask("Обновленная подзадача", "Подзадача эпика", Status.DONE,
                updateEpic.getId());
        updateSubtask.setId(4);
        taskManager.updateTasks(updateTask);
        taskManager.updateEpics(updateEpic);
        taskManager.updateSubTasks(updateSubtask);

        assertEquals(updateTask, taskManager.getTasksById(1), "Ошибка при обновлении Таск");
        assertEquals(updateEpic, taskManager.getEpicsById(3), "Ошибка при обновлении Эпик");
        assertEquals(updateSubtask, taskManager.getSubTasksById(4), "Ошибка при обновлении Сабтаск");
    }

    @Test
    public void checkHistoryAndCheckSizeHistory() {
        creatingAndAddingTaskSubTaskEpic();
        Task testTask = taskManager.getTasksById(2);
        taskManager.getTasksById(1);
        taskManager.getEpicsById(7);
        taskManager.getSubTasksById(5);
        Task testTask1 = taskManager.getSubTasksById(8);
        taskManager.getSubTasksById(4);
        taskManager.getSubTasksById(6);
        taskManager.getSubTasksById(10);
        taskManager.getSubTasksById(9);
        Task testTask2 = taskManager.getTasksById(1);

        List<Task> history = taskManager.getHistory();
        assertEquals(testTask, history.get(0), "Ошибка при соотвествии задач1");
        assertEquals(testTask1, history.get(3), "Ошибка при соответствии задач4");
        assertEquals(testTask2, history.get(8), "Ошибка при соответсвии задач2");
        assertEquals(9, taskManager.getHistory().size());
        Task testTask3 = taskManager.getEpicsById(3);
        assertEquals(testTask3, taskManager.getHistory().get(9), "Ошибка при соответсвии задач3");
        assertEquals(10, taskManager.getHistory().size());
    }

    @Test
    public void checkUpdateEpicStatus() {
        creatingAndAddingTaskSubTaskEpic();
        Subtask newSubtask1 = new Subtask("Подзадача1", "Описание1", Status.DONE, 7);
        assertSame(taskManager.getEpicsById(7).getStatus(), Status.NEW, "Ошибка при обновлении задачи");
        newSubtask1.setId(9);
        taskManager.updateSubTasks(newSubtask1);
        assertSame(taskManager.getEpicsById(7).getStatus(), Status.IN_PROGRESS, "Ошибка при обновлении задачи");
        Subtask newSubtask2 = new Subtask("Подзадача2", "Описание 2", Status.DONE, 7);
        newSubtask2.setId(8);
        taskManager.updateSubTasks(newSubtask2);
        Subtask newSubtask3 = new Subtask("Подзадача3", "Описание 3", Status.DONE, 7);
        newSubtask3.setId(10);
        taskManager.updateSubTasks(newSubtask3);
        assertSame(taskManager.getEpicsById(7).getStatus(), Status.DONE, "Ошибка при обновлении задачи");
    }

    @Test
    void checkTimeOverlapWhenAddAndUpdateTaskSubTaskEpicAndCalculateEpicTime() {
        Task task1 = new Task("Задача1", "Описание1", LocalDateTime.of(2024, 9, 3, 10, 0),
                Duration.ofHours(1));
        taskManager.createTasks(task1);
        Task task2 = new Task("Задача2", "Описание2", LocalDateTime.of(2023, 9, 3, 10, 0),
                Duration.ofHours(1));
        taskManager.createTasks(task2);
        Epic epic1 = new Epic("Эпик1", "Описание Эпика1");
        taskManager.createEpics(epic1);
        int epicId = epic1.getId();
        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", Status.NEW,
                LocalDateTime.of(2025, 10, 10, 13, 0), Duration.ofHours(1), epicId);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", Status.NEW,
                LocalDateTime.of(2025, 10, 10, 16, 0), Duration.ofHours(3), epicId);
        taskManager.createSubTasks(subtask1);
        taskManager.createSubTasks(subtask2);

        assertEquals(taskManager.getTasksById(2), taskManager.getPrioritizedTasks().get(0),
                "Ошибка в приоритете задач");

        assertEquals(epic1.getEndTime(), LocalDateTime.of(2025, 10, 10, 19, 0),
                "Ошибка при расчете заверения эпика");

        Subtask subtask3 = new Subtask("Подзадача3", "Описание подзадачи3", Status.NEW,
                LocalDateTime.of(2025, 10, 11, 16, 0), Duration.ofHours(3), epicId);
        subtask3.setId(5);
        taskManager.updateSubTasks(subtask3);

        assertEquals(epic1.getEndTime(), LocalDateTime.of(2025, 10, 11, 19, 0),
                "Ошибка при расчете завершения эпика, после обновления Сабтаска");
        assertEquals(epic1.getDuration(), Duration.ofHours(30), "Ошибка при расчете продолжительности эпика");

        Task task3 = new Task("Задача1", "Описание1", LocalDateTime.of(2024, 9, 3, 9, 0),
                Duration.ofHours(1));
        Assertions.assertThrows(OverlapTimeException.class, () ->
                        taskManager.createTasks(task3),
                "Задача не может быть добавлена, пересечение по времени");

        Subtask subtask4 = new Subtask("Подзадача3", "Описание подзадачи3", Status.NEW,
                LocalDateTime.of(2025, 10, 11, 19, 0), Duration.ofHours(3), epicId);


        Assertions.assertThrows(OverlapTimeException.class, () ->
                        taskManager.createSubTasks(subtask4),
                "Подзадача не может быть добавлена, пересечение по времени");
    }

    @Test
    void checkDeleteTaskEpicSubTask() {
        creatingAndAddingTaskSubTaskEpic();
        taskManager.getTasksById(1);
        taskManager.getTasksById(2);
        taskManager.getSubTasksById(4);
        taskManager.getEpicsById(7);
        taskManager.getSubTasksById(9);
        taskManager.getEpicsById(3);

        assertEquals(6, taskManager.getHistory().size());
        taskManager.deleteTasksById(2);
        taskManager.deleteSubTasksById(4);
        taskManager.deleteEpicsById(3);
        assertEquals(3, taskManager.getHistory().size());

        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getHistory().size());

        assertTrue(taskManager.getAllTasks().isEmpty(), "Ошибка при удалении всех Таск");
        assertTrue(taskManager.getAllSubTasks().isEmpty(), "Ошибка при удалении всех СабТаск");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Ошибка при удалении всех Эпик");
    }
}
