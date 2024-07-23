package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class TaskHandler extends Handler {

    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathTaskId);
                        if (taskId != -1) {
                            getTaskWithTheCorrectId(httpExchange, taskId);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else if (Pattern.matches("^/tasks", path)) {
                        String pathAllTasks = gson.toJson(manager.listTask());
                        writeResponse(httpExchange, pathAllTasks, 200);
                    } else {
                        writeResponse(httpExchange, "Неверный URL.", 404);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String requestBody = readResponse(httpExchange);
                        if (requestBody != null) {
                            postTaskWithBodyNotNull(httpExchange, requestBody);
                        } else {
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathTaskId);
                        String requestBody = readResponse(httpExchange);
                        if (taskId != -1) {
                            updateTaskWithCorrectId(httpExchange, requestBody);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathTaskId);
                        if (taskId != -1) {
                            deleteTaskWithCorrectBody(httpExchange,taskId);
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

    //вынесено в отдельный метод поиск задачи с корректным идентификтором
    public void getTaskWithTheCorrectId(HttpExchange httpExchange, int id) throws IOException {
        try {
            Optional<Task> task = manager.getTaskById(id);
            writeResponse(httpExchange, gson.toJson(task.get()), 200);
        } catch (Exception e) {
            writeResponse(httpExchange, "Задачи с таким id не существует!", 404);
        }
    }

    //вынесено в отдельный метод обновление задачи с корректным идентификатором
    public void updateTaskWithCorrectId(HttpExchange httpExchange, String requestBody) throws IOException {
        if (requestBody != null) {
            Task t = gson.fromJson(requestBody, Task.class);
            manager.updateTask(t);
            writeResponse(httpExchange, gson.toJson(t), 201);
            System.out.println("Задача успешно обновлена!");
        }  else {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }

    //вынесено в отдельный метод публикация задачи с отделным идентификатором
    public void postTaskWithBodyNotNull(HttpExchange httpExchange, String requestBody) throws IOException {
        Task newTask = gson.fromJson(requestBody, Task.class);
        if (manager.addTask(newTask) != null) {
            writeResponse(httpExchange, gson.toJson(newTask), 201);
            System.out.println("Задача успешно добавлена.");
        } else {
            writeResponse(httpExchange, "Новая задача пересекается с существующими", 406);
        }
    }

    //вынесено в отдельный метод и добавлено обработка исключения при удалении задачи
    public void deleteTaskWithCorrectBody(HttpExchange httpExchange, int taskId) throws IOException {
        try {
            manager.deleteTaskById(taskId);
            String body = "Задача с идентификатором " + taskId + " успешно удалена.";
            writeResponse(httpExchange, body, 200);
        } catch (Exception e) {
            writeResponse(httpExchange, "Задача с таким номером не найдена.", 404);
        }
    }

}
