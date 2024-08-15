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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {

    File testFile;

    @BeforeEach
    void createTestFile() {
        try {
            testFile = File.createTempFile("testFile", ".csv");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла.");
        }
    }
    /*
    Анна, привет! Забыл задать этот вопрос в первом ревью, вдруг сможешь помочь. Проблема следующая:
    в ТЗ предлагают использовать такой метод File.createTempFile() для создания временных файлов.
    Я провел первый тест и файл создался, я мог его открыть и посмотреть что в него записалось. После, я удалил этот
    файл из папки (test) просто через Delete и он перестал мне показываться при проведении следующих тестов, то есть,
    он создается, но я больше не вижу его в папке и не могу открыть для того, что бы посмотреть, как у меня записались
    данные.
    Наставник предложил создавать файл в другой папке testdata, Я делаю так:
    testFile = File.createTempFile("testFile", ".csv", new File("C:\\Java\\" +
                    "IdeaProjects\\java-kanban\\testdata"));
    Таким способом файлы я вижу и могу их открывать и смотреть что в них. Но тесты в GitHub не пропускают этот метод.
    Приходится возвращаться к File.createTempFile("testFile", ".csv").
    Может я с чем то согласился в Idea при первом удалении файла через Delete? или это нормально что я его не вижу?
    Этот метод testFile.deleteOnExit() я убираю)
    Спасибо)
     */

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

        List<String> list1 = new ArrayList<>();

        list1.add("id,type,name,status,desctiption,epic");
        list1.add("1,TASK,Задача 1,NEW,описание 1");
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
            writer.write("id,type,name,status,desctiption,epic\n");
            writer.write("1,TASK,Задача 1,NEW,описание 1\n");
            writer.write("2,EPIC,Epic 1,NEW,авто\n");
            writer.write("4,EPIC,Epic 2,NEW,море\n");
            writer.write("3,SUBTASK,SubTask 1,NEW,Поменять масло,2\n");
            writer.write("5,SUBTASK,SubTask 2_1,NEW,билеты,4\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при запись в файл");
        }

        FileBackedTaskManager managerLoad = FileBackedTaskManager.loadFromFile(testFile);

        Task task11 = new Task(1, "Задача 1", "описание 1", Status.NEW);
        Epic epic11 = new Epic(2, "Epic 1", "авто", Status.NEW);
        Subtask subtask11 = new Subtask(3, "SubTask 1", "Поменять масло", Status.NEW, 2);
        Epic epic22 = new Epic(4, "Epic 2", "море", Status.NEW);
        Subtask subtask22 = new Subtask(5, "SubTask 2_1", "билеты", Status.NEW, 4);

        assertEquals(task11, managerLoad.getTasksById(1));
        assertEquals(epic11, managerLoad.getEpicsById(2));
        assertEquals(subtask11, managerLoad.getSubTasksById(3));
        assertEquals(epic22, managerLoad.getEpicsById(4));
        assertEquals(subtask22, managerLoad.getSubTasksById(5));
    }

    @AfterEach
    void deleteTestFile() {
        testFile.deleteOnExit();
    }
}
