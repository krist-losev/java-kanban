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
                    String requestBody = readResponse(httpExchange);
                    Epic newEpic = gson.fromJson(requestBody, Epic.class);
                    Optional<Epic> id = Optional.of(newEpic);
                    if (id.isPresent() && manager.listEpic().contains(newEpic)) {
                        try {
                            manager.updateEpic(newEpic);
                            writeResponse(httpExchange, gson.toJson(newEpic), 201);
                        } catch (Exception e) {
                            writeResponse(httpExchange,
                                    "Обновленный эпик пересекается с существующими", 406);
                        }
                    } else {
                        try {
                            manager.addEpic(newEpic);
                            writeResponse(httpExchange, gson.toJson(newEpic), 201);
                        } catch (Exception e) {
                            writeResponse(httpExchange, "Новый эпик пересекается с существующими", 406);
                        }
                        break;
                    }
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
