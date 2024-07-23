package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;

import java.util.Optional;
import java.util.regex.Pattern;

public class EpicHandler extends Handler {
    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        try (httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            Epic newEpic;

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathEpicId = path.replaceFirst("/epics/", "");
                        int id = parsePathTaskId(pathEpicId);
                        if (id != -1) {
                            try {
                                Optional<Epic> epic = manager.getEpicById(id);
                                writeResponse(httpExchange, gson.toJson(epic.get()), 200);
                            } catch (Exception e) {
                                writeResponse(httpExchange, "Эпика с таким id не существует!", 404);
                            }
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else if (Pattern.matches("^/epics", path)) {
                        String pathAllEpic = gson.toJson(manager.listEpic());
                        writeResponse(httpExchange, pathAllEpic, 200);
                    } else if (Pattern.matches("^/epics/\\d+/subtasks/\\d+$", path)) {
                        int idEpic = parsePathTaskId(path.split("/")[2]);
                        if (idEpic != 1) {
                            Optional<Epic> epic = manager.getEpicById(idEpic);
                            String allSubtaskEpic = gson.toJson(epic.get().getSubtasksId());
                            writeResponse(httpExchange, allSubtaskEpic, 200);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else {
                        writeResponse(httpExchange, "Неверный URL.", 404);
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        String requestBody = readResponse(httpExchange);
                        if (requestBody != null) {
                            newEpic = gson.fromJson(requestBody, Epic.class);
                            if (manager.addTask(newEpic) != null) {
                                writeResponse(httpExchange, gson.toJson(newEpic), 201);
                                System.out.println("Эпик успешно добавлен.");
                            } else {
                                writeResponse(httpExchange, "Новая задача пересекается с существующими", 406);
                            }
                        } else {
                            httpExchange.sendResponseHeaders(500, 0);
                        }
                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathEpicId = path.replaceFirst("/tasks/", "");
                        int taskId = parsePathTaskId(pathEpicId);
                        String requestBody = readResponse(httpExchange);
                        if (taskId != -1) {
                            if (requestBody != null) {
                                newEpic = gson.fromJson(requestBody, Epic.class);
                                manager.updateTask(newEpic);
                                writeResponse(httpExchange, gson.toJson(newEpic), 201);
                                System.out.println("Эпик успешно обновлен!");
                            }  else {
                                httpExchange.sendResponseHeaders(500, 0);
                            }
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathEpicId = path.replaceFirst("/epics/", "");
                        int id = parsePathTaskId(pathEpicId);
                        if (id != -1) {
                            manager.deleteEpicById(id);
                            String body = "Эпик с идентификатором " + id + " успешно удален.";
                            writeResponse(httpExchange, body, 200);
                        } else {
                            writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
                        }
                    } else {
                        writeResponse(httpExchange, "Получен некорректный идентификатор!", 404);
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
