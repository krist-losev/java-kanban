package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.util.Optional;
import java.util.regex.Pattern;

public class TaskHandler extends Handler {

    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathTaskId);
                        if (taskId != -1) {
                            try {
                                Optional<Task> task = manager.getTaskById(taskId);
                                writeResponse(httpExchange, gson.toJson(task.get()), 200);
                            } catch (Exception e) {
                                writeResponse(httpExchange, "Задачи с таким id не существует!", 404);
                            }
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
                    String requestBody = readResponse(httpExchange);
                    Task newTask = gson.fromJson(requestBody, Task.class);
                    if (manager.listTask().contains(newTask.getIdNumber())) {
                        try {
                            manager.updateTask(newTask);
                            writeResponse(httpExchange, gson.toJson(newTask), 201);
                            System.out.println("Задача успешно обновлена!");
                        } catch (Exception e) {
                            writeResponse(httpExchange, "Обновленная задача пересекается с существующими", 406);
                        }
                    } else {
                        try {
                            manager.addTask(newTask);
                            writeResponse(httpExchange, gson.toJson(newTask), 201);
                        } catch (Exception e) {
                            writeResponse(httpExchange, "Новая задача пересекается с существующими", 406);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathTaskId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathTaskId);
                        if (taskId != -1) {
                            manager.deleteTaskById(taskId);
                            String body = "Задача с идентификатором " + taskId + " успешно удалена.";
                            writeResponse(httpExchange, body, 200);
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
        } finally {
                httpExchange.close();
        }
    }
}
