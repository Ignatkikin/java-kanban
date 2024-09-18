
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

public class EpicHandlerTest {
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
    }

    @Test
    public void checkGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "description1");

        manager.createEpics(epic);
        Subtask subtask = new Subtask("SabTask1", "Сабтаск1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30), epic.getId());
        manager.createSubTasks(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }

    @Test
    public void checkPostEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание Epica");
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Epic1", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void checkPostUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "ОписаниеЭпика1");
        manager.createEpics(epic);
        Epic updateEpic = new Epic("Epic2", "Обновленное описание Эпика");
        String epicJson = gson.toJson(updateEpic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное кол-во задач");
        assertEquals("Epic2", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }


    @Test
    public void checkDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic1", "Описание1");
        manager.createEpics(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        assertEquals(0, tasksFromManager.size(), "Некорректное кол-во задач");
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }
}

