package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.OverlapTimeException;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                String[] path = exchange.getRequestURI().getPath().split("/");
                if (path.length <= 2) {
                    sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                } else if (path.length == 3) {
                    try {
                        int epicId = Integer.parseInt(path[2]);
                        Epic epic = taskManager.getEpicsById(epicId);
                        sendText(exchange, gson.toJson(epic), 200);
                    } catch (NotFoundException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    try {
                        int subtaskId = Integer.parseInt(path[2]);
                        sendText(exchange, gson.toJson(taskManager.getSubTaskFromEpic(subtaskId)), 200);
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
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic != null) {
                            taskManager.createEpics(epic);
                            sendNoText(exchange, 201);
                        } else {
                            sendText(exchange, "пустая задача", 400);
                        }
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (epic != null) {
                            int epicId = Integer.parseInt(path[2]);
                            epic.setId(epicId);
                            taskManager.updateEpics(epic);
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
                    int epicId = Integer.parseInt(path[2]);
                    taskManager.deleteEpicsById(epicId);
                    sendText(exchange, "Эпик успешно удален", 200);
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
