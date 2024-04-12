package manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager manager;

    @BeforeEach
    void creatingAndAddingTaskSubTaskEpic() {
        manager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Выучить теорию 5 спринта");
        Task task2 = new Task("Задача 2", "Сдать тз5");
        manager.createTasks(task1);
        manager.createTasks(task2);

        Epic epic1 = new Epic("Epic 1", "Починить авто");
        manager.createEpics(epic1);
        int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        Subtask subtask2 = new Subtask("SubTask 2", "Починить дверь", Status.NEW, idEpic1);
        Subtask subtask3 = new Subtask("SubTask 3", "Поменять лампочки", Status.NEW, idEpic1);
        manager.createSubTasks(subtask1);
        manager.createSubTasks(subtask2);
        manager.createSubTasks(subtask3);

        Epic epic2 = new Epic("Epic 2", "Съездить на море");
        manager.createEpics(epic2);
        int idEpic2 = epic2.getId();
        Subtask subtask2_1 = new Subtask("SubTask 2_1", "Купить билеты", Status.NEW, idEpic2);
        Subtask subtask2_2 = new Subtask("SubTask 2_2", "Собрать чемодан", Status.NEW, idEpic2);
        Subtask subtask2_3 = new Subtask("SubTask 2_3", "Попросить друга следить за котом", Status.NEW, idEpic2);
        manager.createSubTasks(subtask2_1);
        manager.createSubTasks(subtask2_2);
        manager.createSubTasks(subtask2_3);
    }

    @Test
    public void cheakAddAllTasks() {
        assertFalse(manager.getAllTasks().isEmpty(), "Ошибка добавления задач");
        assertFalse(manager.getAllEpics().isEmpty(),"Ошибка добавления Эпиков");
        assertFalse(manager.getAllSubTasks().isEmpty(),"Ошибка добавления Сабтаск");
    }

    @Test
    public void fieldsMustBeEqualBeforeAndAfterAddingToTheManager() {
        Task testTask = new Task("Задача 3", "Описание");
        String name = testTask.getName();
        String description = testTask.getDescription();
        Status status = testTask.getStatus();
        manager.createTasks(testTask);
        int testTaskid = testTask.getId();

        assertEquals(manager.getTasksById(testTaskid).getName(), name,
                "Ошибка с совподением имени до и после добавления в manager");
        assertEquals(manager.getTasksById(testTaskid).getDescription(), description,
                "Ошибка с совподением описания до и после добавления в manager");
        assertEquals(manager.getTasksById(testTaskid).getStatus(), status,
                "Ошибка с совподением статуса до и после добавления в manager");
    }

    @Test
    public void checkUpdateTasksEpicSubTask() {
        Task updateTask = new Task("Обновленная задача", "Обновленное описание");
        updateTask.setId(1);
        Epic updateEpic = new Epic("Обновленый Эпик", "Подзадача Эпика");
        updateEpic.setId(3);
        Subtask updateSubtask = new Subtask("Обновленная подзадача", "Подзадача эпика", Status.DONE, updateEpic.getId());
        updateSubtask.setId(4);
        manager.updateTasks(updateTask);
        manager.updateEpics(updateEpic);
        manager.updateSubTasks(updateSubtask);

        assertEquals(updateTask, manager.getTasksById(1), "Ошибка при обновлении Таск");
        assertEquals(updateEpic, manager.getEpicsById(3), "Ошибка при обновлении Эпик");
        assertEquals(updateSubtask, manager.getSubTasksById(4), "Ошибка при обновлении Сабтаск");

    }

    @Test
    public void checkHistoryAndCheckSizeHistory() {
        Task testTask = manager.getTasksById(2);
        manager.getTasksById(1);
        manager.getEpicsById(7);
        manager.getSubTasksById(5);
        manager.getSubTasksById(4);
        manager.getSubTasksById(4);
        manager.getSubTasksById(6);
        manager.getSubTasksById(10);
        manager.getSubTasksById(9);
        Task testTask1 = manager.getTasksById(1);

        List<Task> history = manager.getHistory();
        assertEquals(testTask, history.get(0), "Ошибка при соотвествии задач1");
        assertEquals(testTask1, history.get(9), "Ошибка при соответсвии задач2");
        assertEquals(10, manager.getHistory().size());
        Task testTask2 = manager.getEpicsById(3);
        assertEquals(testTask2, manager.getHistory().get(9), "Ошибка при соответсвии задач3");
    }

    @Test
    public void checkUpdateEpicStatus() {
        Subtask newSubtask1 = new Subtask("Подзадача1", "Описание1", Status.DONE, 7);
        assertSame(manager.getEpicsById(7).getStatus(), Status.NEW, "Ошибка при обновлении задачи");
        newSubtask1.setId(9);
        manager.updateSubTasks(newSubtask1);
        assertSame(manager.getEpicsById(7).getStatus(), Status.IN_PROGRESS, "Ошибка при обновлении задачи");
        Subtask newSubtask2 = new Subtask("Подзадача2", "Описание 2", Status.DONE, 7);
        newSubtask2.setId(8);
        manager.updateSubTasks(newSubtask2);
        Subtask newSubtask3 = new Subtask("Подзадача3", "Описание 3", Status.DONE, 7);
        newSubtask3.setId(10);
        manager.updateSubTasks(newSubtask3);
        assertSame(manager.getEpicsById(7).getStatus(), Status.DONE, "Ошибка при обновлении задачи");
    }

     @Test
     void checkDeleteTaskEpicSubTask() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpics();

        assertTrue(manager.getAllTasks().isEmpty(), "Ошибка при удалении всех Таск");
        assertTrue(manager.getAllSubTasks().isEmpty(),"Ошибка при удалении всех СабТаск");
        assertTrue(manager.getAllEpics().isEmpty(), "Ошибка при удалении всех Эпик");
    }
}