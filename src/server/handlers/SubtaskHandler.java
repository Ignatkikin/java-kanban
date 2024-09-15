package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.OverlapTimeException;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                String[] path = exchange.getRequestURI().getPath().split("/");

                if (path.length <= 2) {
                    sendText(exchange, gson.toJson(taskManager.getAllSubTasks()), 200);
                } else {
                    try {
                        int subtaskId = Integer.parseInt(path[2]);
                        Subtask subtask = taskManager.getSubTasksById(subtaskId);
                        sendText(exchange, gson.toJson(subtask), 200);
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
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (subtask != null) {
                            taskManager.createSubTasks(subtask);
                            sendNoText(exchange, 201);
                        } else {
                            sendText(exchange, "пустая задача", 400);
                        }
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (subtask != null) {
                            int subtaskId = Integer.parseInt(path[2]);
                            subtask.setId(subtaskId);
                            taskManager.updateSubTasks(subtask);
                            sendNoText(exchange, 201);
                        } else {
                            sendText(exchange, "пустая задача", 400);
                        }
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    }
                }
                break;
            }
            case "DELETE": {
                try {
                    String[] path = exchange.getRequestURI().getPath().split("/");
                    int subtaskId = Integer.parseInt(path[2]);
                    taskManager.deleteSubTasksById(subtaskId);
                    sendText(exchange, "Сабтаск успешно удален", 200);
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
