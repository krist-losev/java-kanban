package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Subtask;

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
                            try {
                                Optional<Subtask> subtask = manager.getSubtaskById(subtaskId);
                                writeResponse(httpExchange, gson.toJson(subtask.get()), 200);
                            } catch (Exception e) {
                                writeResponse(httpExchange, "Подзадача с данным id не найдена", 200);
                            }
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
                    String requestBody = readResponse(httpExchange);
                    Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
                    if (newSubtask.getIdNumber() == 0 && manager.listSubtask().contains(newSubtask)) {
                        try {
                            manager.updateSubtask(newSubtask);
                            writeResponse(httpExchange, gson.toJson(newSubtask), 201);
                            System.out.println("Задача успешно обновлена!");
                        } catch (Exception e) {
                            writeResponse(httpExchange,
                                    "Обновленная задача пересекается с существующими", 406);
                        }
                    } else {
                        try {
                            manager.addSubtask(newSubtask);
                            writeResponse(httpExchange, gson.toJson(newSubtask), 201);
                        } catch (Exception e) {
                            writeResponse(httpExchange, "Новая задача пересекается с существующими", 406);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathSubTaskId = path.replaceFirst("/subtasks/", "");
                        int subtaskId = parsePathTaskId(pathSubTaskId);
                        if (subtaskId != -1) {
                            try {
                                manager.deleteSubtaskById(subtaskId);
                                writeResponse(httpExchange, "Задача успешно удалена.", 200);
                            } catch (Exception e) {
                                writeResponse(httpExchange, "Задачи с таким id не существует!", 404);
                            }
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

}
