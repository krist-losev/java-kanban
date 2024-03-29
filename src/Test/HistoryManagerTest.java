package Test;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void beforeEach () {
        taskManager = Managers.getDefault();

    }

    @Test
    void addTaskEpicSubtaskToHistory () {
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Epic newEpic = new Epic("Ep2", "D1", 2);
        taskManager.addEpic(newEpic);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 3, 2);
        newEpic.addSubtasksInEpic(3);
        taskManager.addSubtask(newSubtask);
        taskManager.getEpicById(newEpic.getIdNumber());
        taskManager.getSubtaskById(newSubtask.getIdNumber());
        taskManager.getTaskById((newTask.getIdNumber()));
        List<Task> history = taskManager.getHistory();
        assertEquals(3, history.size());
    }

    @Test
    void HistorySizeEqualsTen () {
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Epic newEpic = new Epic("Ep2", "D1", 2);
        taskManager.addEpic(newEpic);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 3, 2);
        newEpic.addSubtasksInEpic(3);
        taskManager.addSubtask(newSubtask);
        taskManager.getEpicById(newEpic.getIdNumber());
        taskManager.getSubtaskById(newSubtask.getIdNumber());
        taskManager.getTaskById((newTask.getIdNumber()));
        taskManager.getEpicById(newEpic.getIdNumber());
        taskManager.getSubtaskById(newSubtask.getIdNumber());
        taskManager.getTaskById((newTask.getIdNumber()));
        taskManager.getEpicById(newEpic.getIdNumber());
        taskManager.getSubtaskById(newSubtask.getIdNumber());
        taskManager.getTaskById((newTask.getIdNumber()));
        taskManager.getEpicById(newEpic.getIdNumber());
        taskManager.getTaskById((newTask.getIdNumber()));
        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size());
    }

@Test
    void addTask_TheTaskDoesNotChangeInHistory() {
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        taskManager.getTaskById((newTask.getIdNumber()));
        List<Task> history1 = taskManager.getHistory();
        Task updateTask = new Task("UpdateTask1", "D1", Status.IN_PROGRESS, 1);
        taskManager.updateTask(updateTask);
        taskManager.getTaskById((updateTask.getIdNumber()));
        List<Task> history = taskManager.getHistory();
        assertEquals(history, history1);
    }


}