
package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
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
                        int taskId = Integer.parseInt(path[2]);
                        Task task = taskManager.getTasksById(taskId);
                        sendText(exchange, gson.toJson(task), 200);
                    } catch (NotFoundException e) {
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
                        if (task != null) {
                            taskManager.createTasks(task);
                            sendNoText(exchange, 201);
                        } else {
                            sendText(exchange, "пустая задача", 400);
                        }
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        Task task = gson.fromJson(body, Task.class);
                        if (task != null) {
                            int taskId = Integer.parseInt(path[2]);
                            task.setId(taskId);
                            taskManager.updateTasks(task);
                            sendNoText(exchange, 201);
                        } else {
                            sendText(exchange, "пустая задача", 400);
                        }
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange);
                    }
                }
                break;
            }
            case "DELETE": {
                try {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    int taskId = Integer.parseInt(path[2]);
                    taskManager.deleteTasksById(taskId);
                    sendText(exchange, "Задача успешна удалена", 200);
                } catch (NumberFormatException e) {
                    sendNotFound(exchange);
                }
                break;
            }
            default:
                sendText(exchange, "не верный метод запроса", 405);
        }

    }
}




