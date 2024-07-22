package server;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager, Managers.getGson()));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, Managers.getGson()));
        server.createContext("/epics", new EpicHandler(taskManager, Managers.getGson()));
        server.createContext("/history", new HistoryHandler(taskManager, Managers.getGson()));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, Managers.getGson()));
    }

    public void startServer() {
        System.out.println("Старт сервера на порту: " + PORT);
        System.out.println("http://localhost:" + PORT);
        server.start();
    }

    public void stopServer() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public static void main(String[] args) throws IOException {
        Task task0 = new Task("Съесть сыр", "Пообедать", Status.NEW,
                LocalDateTime.of(2024, 5, 3, 12, 30), Duration.ofMinutes(10));
        Task task = new Task("Искупаться", "Жариться в адском душе", Status.DONE,
                LocalDateTime.of(2024, 5, 1, 16, 3), Duration.ofMinutes(15));
        Subtask subtask0 = new Subtask("Дописать программу", "писааать", Status.DONE, 6,
                LocalDateTime.of(2024, 5, 10, 20, 20), Duration.ofMinutes(60), 3);
        Subtask subtask1 = new Subtask("Проверить код", "Пропустить через дебагер", Status.IN_PROGRESS, 7,
                LocalDateTime.of(2024, 5, 4, 20, 20), Duration.ofMinutes(60),3);

        List<Subtask> s1 = new ArrayList<>();
        s1.add(subtask0);
        s1.add(subtask1);

        Epic e1 = new Epic("Сдать тз4", "Доделать дз и сдать его", 3);
        Epic e2 = new Epic("Подготовиться на работу", "Опять вставать в пять утра:(", 4);
        TaskManager tm = Managers.getDefault();
        tm.addTask(task0);
        tm.addTask(task);
        tm.addEpic(e1);
        tm.addEpic(e2);
        tm.addSubtask(subtask0);
        tm.addSubtask(subtask1);
        HttpTaskServer serverNew = new HttpTaskServer(tm);
        serverNew.startServer();
    }
}
