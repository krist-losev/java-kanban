package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class SubtaskHandler extends Handler {

    public SubtaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/subtasks/", "");
                        int subtaskId = parsePathTaskId(pathTaskId);
                        if (subtaskId != -1) {
                            getSubtaskWithTheCorrectId(httpExchange, subtaskId);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else if (Pattern.matches("^/subtasks", path)) {
                        String pathAllSubtask = gson.toJson(manager.listSubtask());
                        writeResponse(httpExchange, pathAllSubtask, 200);
                    } else {
                        writeResponse(httpExchange, "Неверный URL.", 404);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/subtasks$", path)) {
                        String requestBody = readResponse(httpExchange);
                        if (requestBody != null) {
                            postSubtaskWithBodyNotNull(httpExchange, requestBody);
                        } else {
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathSubId = path.replaceFirst("/subtasks/", "");
                        int taskId = parsePathTaskId(pathSubId);
                        String requestBody = readResponse(httpExchange);
                        if (taskId != -1) {
                            updateSubtaskWithCorrectId(httpExchange, requestBody);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathSubTaskId = path.replaceFirst("/subtasks/", "");
                        int subtaskId = parsePathTaskId(pathSubTaskId);
                        if (subtaskId != -1) {
                            deleteSubtaskWithCorrectId(httpExchange, subtaskId);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else {
                        writeResponse(httpExchange, "Неверный URL.", 404);
                    }
                    break;
                }
                default: {
                    String body = "Неправильный метод. Ожидаемые методы: GET, POST, DELETE!";
                    writeResponse(httpExchange, body, 405);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //вынесено в отдельный метод поиск задачи с корреткным идентификатором
    public void getSubtaskWithTheCorrectId(HttpExchange httpExchange, int id) throws IOException {
        try {
            Optional<Subtask> subtask = manager.getSubtaskById(id);
            writeResponse(httpExchange, gson.toJson(subtask.get()), 200);
        } catch (Exception e) {
            writeResponse(httpExchange, "Подзадача с данным id не найдена", 200);
        }
    }

    //вынесено в отдельный метод обновление задачи с корректным идентификатором
    public void updateSubtaskWithCorrectId(HttpExchange httpExchange, String requestBody) throws IOException {
        if (requestBody != null) {
            Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
            manager.updateTask(newSubtask);
            writeResponse(httpExchange, gson.toJson(newSubtask), 201);
            System.out.println("Эпик успешно обновлен!");
        }  else {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }

    //вынесено в отдельный метод публикация задачи с корректным идентификатором
    public void postSubtaskWithBodyNotNull(HttpExchange httpExchange, String requestBody) throws IOException {
        Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
        if (manager.addTask(newSubtask) != null) {
            writeResponse(httpExchange, gson.toJson(newSubtask), 201);
            System.out.println("Подзадача успешно добавлен.");
        } else {
            writeResponse(httpExchange, "Новая задача пересекается с существующими", 406);
        }
    }

    //вынесено в отдельный метод удаление задачи с корректным идентификатором
    public void deleteSubtaskWithCorrectId(HttpExchange httpExchange, int subtaskId) throws IOException {
        try {
            manager.deleteSubtaskById(subtaskId);
            writeResponse(httpExchange, "Задача успешно удалена.", 200);
        } catch (Exception e) {
            writeResponse(httpExchange, "Задачи с таким id не существует!", 404);
        }
    }

}
