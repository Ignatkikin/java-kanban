package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File testFile;

    @BeforeEach
    void createTestFile() {
        try {
            testFile = File.createTempFile("testFile", ".csv");
            taskManager = new FileBackedTaskManager(testFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла.");
        }
    }

    @Test
    void creatingAndSavingTaskToFile() {
        FileBackedTaskManager manager = new FileBackedTaskManager(testFile);
        Task task1 = new Task("Задача 1", "описание 1");
        manager.createTasks(task1);
        Epic epic1 = new Epic("Epic 1", "авто");
        manager.createEpics(epic1);
        int idEpic1 = epic1.getId();
        Subtask subtask1 = new Subtask("SubTask 1", "Поменять масло", Status.NEW, idEpic1);
        manager.createSubTasks(subtask1);

        Epic epic2 = new Epic("Epic 2", "море");
        manager.createEpics(epic2);
        int idEpic2 = epic2.getId();
        Subtask subtask2_1 = new Subtask("SubTask 2_1", "билеты", Status.NEW, idEpic2);
        manager.createSubTasks(subtask2_1);

        Task taskTime = new Task("Задача", "Смотрим",
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofHours(1));
        manager.createTasks(taskTime);

        List<String> list1 = new ArrayList<>();

        list1.add("id,type,name,status,desctiption,epic,datatime,duration");
        list1.add("1,TASK,Задача 1,NEW,описание 1");
        list1.add("6,TASK,Задача,NEW,Смотрим,2020-10-10T10:00,PT1H");
        list1.add("2,EPIC,Epic 1,NEW,авто");
        list1.add("4,EPIC,Epic 2,NEW,море");
        list1.add("3,SUBTASK,SubTask 1,NEW,Поменять масло,2");
        list1.add("5,SUBTASK,SubTask 2_1,NEW,билеты,4");


        List<String> list2 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(testFile, StandardCharsets.UTF_8))) {

            while (reader.ready()) {
                list2.add(reader.readLine());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла.");
        }
        assertEquals(list1, list2);
    }

    @Test
    void testLoadFromFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,desctiption,epic,datatime,duration\n");
            writer.write("1,TASK,Задача 1,NEW,описание 1\n");
            writer.write("2,EPIC,Epic 1,NEW,авто\n");
            writer.write("4,EPIC,Epic 2,NEW,море\n");
            writer.write("3,SUBTASK,SubTask 1,NEW,Поменять масло,2\n");
            writer.write("5,SUBTASK,SubTask 2_1,NEW,билеты,4\n");
            writer.write("6,TASK,Задача,NEW,Смотрим,2020-10-10T10:00,PT1H\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при запись в файл");
        }

        FileBackedTaskManager managerLoad = FileBackedTaskManager.loadFromFile(testFile);

        Task task11 = new Task(1, "Задача 1", "описание 1", Status.NEW);
        Epic epic11 = new Epic(2, "Epic 1", "авто", Status.NEW);
        Subtask subtask11 = new Subtask(3, "SubTask 1", "Поменять масло", Status.NEW, 2);
        Epic epic22 = new Epic(4, "Epic 2", "море", Status.NEW);
        Subtask subtask22 = new Subtask(5, "SubTask 2_1", "билеты", Status.NEW, 4);
        Task task22 = new Task(6, "Задача", "Смотрим", Status.NEW,
                LocalDateTime.of(2020, 10, 10, 10, 0), Duration.ofHours(1));

        assertEquals(task11, managerLoad.getTasksById(1));
        assertEquals(epic11, managerLoad.getEpicsById(2));
        assertEquals(subtask11, managerLoad.getSubTasksById(3));
        assertEquals(epic22, managerLoad.getEpicsById(4));
        assertEquals(subtask22, managerLoad.getSubTasksById(5));
        assertEquals(task22, managerLoad.getTasksById(6));
    }

    @AfterEach
    void deleteTestFile() {
        testFile.deleteOnExit();
    }
}
