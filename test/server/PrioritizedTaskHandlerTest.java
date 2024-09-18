
package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

public class PrioritizedTaskHandlerTest {

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
    public void ckeckGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Task1", "Описание1",
                LocalDateTime.of(2024, 10, 10, 10, 0), Duration.ofMinutes(30));
        Task task1 = new Task("Task2", "Описание2",
                LocalDateTime.of(2023, 10, 10, 10, 0), Duration.ofMinutes(30));
        Task task2 = new Task("Task3", "Описание3",
                LocalDateTime.of(2022, 10, 10, 10, 0), Duration.ofMinutes(30));
        manager.createTasks(task);
        manager.createTasks(task1);
        manager.createTasks(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        List<Task> managerPrioritized = manager.getPrioritizedTasks();
        assertEquals(200, response.statusCode());
        assertEquals(prioritizedList.get(2), managerPrioritized.get(2));
        assertNotNull(prioritizedList, "Пустая история");
        assertEquals(3, prioritizedList.size(), "Некорректное кол-во задач");
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }
}

