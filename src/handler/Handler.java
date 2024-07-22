package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public abstract class Handler implements HttpHandler {

    protected TaskManager manager;
    protected Gson gson;

    public Handler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public abstract void handle(HttpExchange httpExchange) throws IOException;

    protected String readResponse(HttpExchange httpExchange) throws IOException {
           return new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void writeResponse(HttpExchange httpExchange, String responseText, int code) throws IOException {
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.close();
    }

    protected int parsePathTaskId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }


}
