
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
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
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

public class SubtaskHandlerTest {
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
    public void checkGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание эпика1");
        manager.createEpics(epic);
        Subtask subtask = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    public void checkPostSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание эпика1");
        manager.createEpics(epic);
        Subtask subtask = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllSubTasks();
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Сабтаск1", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void checkPostUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание эпика1");
        manager.createEpics(epic);
        Subtask subtask = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.of(2024, 9, 13, 10, 0), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask);
        Subtask updateSubtask = new Subtask("Сабтаск2", "Описание сабтаска2", Status.NEW,
                LocalDateTime.of(2024, 9, 13, 18, 0), Duration.ofMinutes(30), epic.getId());
        String subtaskJson = gson.toJson(updateSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = manager.getAllSubTasks();
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Сабтаск2", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void checkPostSubtaskOverlapTime() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание эпика1");
        manager.createEpics(epic);
        Subtask subtask = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask);
        Subtask subtask1 = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.of(2024, 10, 11, 10, 0), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.of(2024, 10, 10, 9, 0), Duration.ofMinutes(90), epic.getId());
        String subtaskJson = gson.toJson(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

        List<Task> prioritizedTasksList = manager.getPrioritizedTasks();

        assertEquals(2, prioritizedTasksList.size(), "Некорректное кол-во задач");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание эпика1");
        manager.createEpics(epic);
        Subtask subtask = new Subtask("Сабтаск1", "Описание сабтаска1", Status.NEW,
                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> subtasksFromManager = manager.getAllSubTasks();
        assertEquals(0, subtasksFromManager.size(), "Некорректное кол-во задач");
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }
}

