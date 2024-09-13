package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.adapters.DurationAdapter;
import server.adapters.LocalDateTimeAdapter;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskHandlerTest {

    TaskManager manager;
    HttpTaskServer server;
    Gson gson;

    @BeforeEach
    public void startServer() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        server.start();
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
    }

    @Test
    public void checkGetTask() throws IOException, InterruptedException {
        Task task = new Task("Task1", "description1",
                LocalDateTime.now(), Duration.ofMinutes(5));

        manager.createTasks(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void checkPostTask() throws IOException, InterruptedException {
        Task task = new Task("Task1", "description1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Task1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void checkPostUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "description1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createTasks(task1);

        Task task2 = new Task("Task2", "description2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Task2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void checkPostTaskOverlapTime() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "description1",
                LocalDateTime.of(2020, 06, 10, 10, 0), Duration.ofMinutes(30));
        manager.createTasks(task1);
        Task task2 = new Task("Task2", "description2",
                LocalDateTime.of(2020, 06, 10, 14, 0), Duration.ofMinutes(30));
        manager.createTasks(task2);

        Task task3 = new Task("Task3", "description2",
                LocalDateTime.of(2020, 06, 10, 10, 29), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task3);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

        List<Task> prioritizedTasksList = manager.getPrioritizedTasks();

        assertEquals(2, prioritizedTasksList.size(), "Некорректное кол-во задач");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Task1", "description1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        manager.createTasks(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(0, tasksFromManager.size(), "Некорректное кол-во задач");
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

}
