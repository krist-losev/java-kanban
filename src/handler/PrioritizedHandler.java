package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.util.regex.Pattern;

public class PrioritizedHandler  extends Handler {
    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            if (method.equals("GET")) {
                if (Pattern.matches("^/prioritized$", path)) {
                    String response = gson.toJson(manager.getPrioritizedTasks());
                    writeResponse(httpExchange, response, 200);
                } else {
                    writeResponse(httpExchange, "Неверный URL", 200);
                }
            } else {
                String body = "Неправильный метод. Ожидаемые методы: GET.";
                writeResponse(httpExchange, body, 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
