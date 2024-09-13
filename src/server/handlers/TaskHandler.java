
package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.OverlapTimeException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length <= 2) {
                    sendText(exchange, gson.toJson(taskManager.getAllTasks()), 200);
                } else {
                    try {
                        Task task = taskManager.getTasksById(Integer.parseInt(path[2]));
                        sendText(exchange, gson.toJson(task), 200);
                    } catch (RuntimeException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            }
            case "POST": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                if (path.length <= 2) {
                    try {
                        Task task = gson.fromJson(body, Task.class);
                        taskManager.createTasks(task);
                        sendNoText(exchange, 201);
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    } catch (NullPointerException e) {
                        sendText(exchange, "пустая задача", 400);
                    }
                } else {
                    try {
                        Task task = gson.fromJson(body, Task.class);
                        task.setId(Integer.parseInt(path[2]));
                        taskManager.updateTasks(task);
                        sendNoText(exchange, 201);
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    } catch (NullPointerException e) {
                        sendText(exchange, "пустая задача", 400);
                    }
                }
                break;
            }
            case "DELETE": {
                try {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    taskManager.deleteTasksById(Integer.parseInt(path[2]));
                    sendText(exchange, "Задача успешна удалена", 200);
                } catch (NumberFormatException | NullPointerException e) {
                    sendNotFound(exchange);
                }
                break;
            }
            default:
                sendText(exchange, "не верный метод запроса", 405);
        }

    }
}




