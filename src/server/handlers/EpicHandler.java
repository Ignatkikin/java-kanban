package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
                        Epic epic = taskManager.getEpicsById(Integer.parseInt(path[2]));
                        sendText(exchange, gson.toJson(epic), 200);
                    } catch (RuntimeException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    try {
                        sendText(exchange, gson.toJson(taskManager.getSubTaskFromEpic(Integer.parseInt(path[2]))), 200);
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
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.createEpics(epic);
                        sendNoText(exchange, 201);
                    } catch (OverlapTimeException e) {
                        sendHasInteractions(exchange);
                    } catch (NullPointerException e) {
                        sendText(exchange, "пустая задача", 400);
                    }
                } else {
                    try {
                        Epic epic = gson.fromJson(body, Epic.class);
                        epic.setId(Integer.parseInt(path[2]));
                        taskManager.updateEpics(epic);
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
                    taskManager.deleteEpicsById(Integer.parseInt(path[2]));
                    sendText(exchange, "Эпик успешно удален", 200);
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
