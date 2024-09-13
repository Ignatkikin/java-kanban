
package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.HttpTaskServer;
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

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        /*
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        server.start();

        Task task = new Task("Test", "TaskTest1", LocalDateTime.now(), Duration.ofHours(1));
        manager.createTasks(task);
        int id = task.getId();
        /*

        String taskJason = gson.toJson(task);

            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJason)).build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        }

        final Task taskFromMnager = gson.fromJson(response.body(),new TypeToken<Task> () {}.getType());
        System.out.println(taskFromMnager);

        System.out.println(response.body());
        System.out.println(response.statusCode());

 */


    }
}



