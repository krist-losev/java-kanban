import com.google.gson.Gson;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {
    protected final TaskManager taskManager = Managers.getDefault();
    protected final Gson gson = Managers.getGson();
    protected final HttpTaskServer server = new HttpTaskServer(taskManager);
    protected final String URL = "http://localhost:8080";

    public HttpServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        server.startServer();
    }

    @AfterEach
    public void shutDown() {
        server.stopServer();
    }

    @Test
    void testTaskAdd() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW,
                LocalDateTime.of(2021, 11, 12, 16, 16), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        URI url = URI.create(URL + "/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson)).
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.listTask();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size());
        assertNotEquals("Test 2", tasksFromManager.get(0).getNameTask(), "Некорректное имя задачи");
    }

    @Test
    void testGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1", Status.NEW,
                LocalDateTime.of(2020, 11, 12, 16, 16), Duration.ofMinutes(5));
        taskManager.addTask(task);
        URI url = URI.create(URL + "/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create(URL + "/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder().
                uri(url1).
                GET().
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void testGetArrayTask() throws IOException, InterruptedException {
        Task e = new Task("Test 1", "Testing task 1", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 11, 12, 14, 15), Duration.ofMinutes(4));
        Task e1 = new Task("222", "fff", Status.NEW,
                LocalDateTime.of(2015, 11, 16, 02, 00), Duration.ofMinutes(16));
        taskManager.addTask(e);
        taskManager.addTask(e1);
        URI url = URI.create(URL + "/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();
        List<Task> tasks = taskManager.listTask();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, tasks.size());
    }

    @Test
    void testGetEpic() throws IOException, InterruptedException {
        Epic e = new Epic("Test 1", "Testing task 1", 1);
        taskManager.addEpic(e);
        URI url = URI.create(URL + "/epics");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        URI url1 = URI.create(URL + "/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder().
                uri(url1).
                GET().
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
    }

    @Test
    void testGetArrayEpic() throws IOException, InterruptedException {
        Epic e = new Epic("Test 1", "Testing task 1", 1);
        Epic e1 = new Epic("222", "fff", 2);
        taskManager.addEpic(e);
        taskManager.addEpic(e1);
        URI url = URI.create(URL + "/epics");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(url).
                GET().
                build();
        List<Epic> epics = taskManager.listEpic();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, epics.size());

        URI url1 = URI.create(URL + "/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder().
                uri(url1).
                GET().
                build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void crossingTask() throws IOException, InterruptedException {
        Task e = new Task("Test 1", "Testing task 1", Status.IN_PROGRESS,
                LocalDateTime.of(2012, 11, 12, 14, 15), Duration.ofMinutes(4));
        Task e1 = new Task("222", "fff", Status.NEW,
                LocalDateTime.of(2012, 11, 12, 14, 15), Duration.ofMinutes(4));

        URI url = URI.create(URL + "/tasks");
        HttpClient client = HttpClient.newHttpClient();
        taskManager.addTask(e1);
        String taskJson0 = gson.toJson(e1);
        HttpRequest request0 = HttpRequest.newBuilder().
                uri(url).
                POST(HttpRequest.BodyPublishers.ofString(taskJson0)).
                build();
        HttpResponse<String> response0 = client.send(request0, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response0.statusCode());
    }


}



