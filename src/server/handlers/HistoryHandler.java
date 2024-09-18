package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> history = taskManager.getHistory();
            if (!history.isEmpty()) {
                String historyJson = gson.toJson(history);
                sendText(exchange, historyJson, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendText(exchange, "не верный метод запроса", 405);
        }
    }
}
