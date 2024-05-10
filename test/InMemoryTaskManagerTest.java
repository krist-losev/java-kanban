import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.Optional;

public class InMemoryTaskManagerTest extends TaskManagerTest <InMemoryTaskManager> {

    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask_TasksWithTheSameIDsAreEqual() {
        Task expectedTask = new Task("Task1", "D1", Status.NEW, 1);
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Optional<Task> actualTask = taskManager.getTaskById(1); //исправить тесты
        Assertions.assertEquals(expectedTask, actualTask.get());
    }

    @Test
    void addEpic_EpicsWithTheSameIDsAreEqual() {
        Epic expectedEpic = new Epic("Ep2", "D1", 1);
        Epic newEpic = new Epic("Ep2", "D1", 1);
        taskManager.addEpic(newEpic);
        Optional<Epic> actualEpic = taskManager.getEpicById(1); //исправить тесты
        Assertions.assertEquals(expectedEpic, actualEpic.get());
    }

    @Test
    void addSubtask_SubtasksWithTheSameIDsAreEqual() {
        Subtask expectedSubtask = new Subtask("St1", "D1", Status.NEW, 2, 1);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 2, 1);
        Epic nEpic = new Epic("E", "D", 1);
        taskManager.addEpic(nEpic);
        nEpic.addSubtasksInEpic(2);
        taskManager.addSubtask(newSubtask);
        Optional<Subtask> actualSubtask = taskManager.getSubtaskById(2); //исправить тесты
        Assertions.assertEquals(expectedSubtask, actualSubtask.get());
    }

    @Test
    void addTask_TheSpecifiedIDsAndTheGeneratedIDsDoNotConflict() {
        Task newTask1 = new Task("Task2", "D2", Status.NEW);
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        taskManager.addTask(newTask1);
        Assertions.assertNotEquals(newTask1, newTask);
    }

    @Test
    void inMemoryTaskManagerAddTaskEpicSubtaskAndGet() {
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Epic newEpic = new Epic("Ep2", "D1", 2);
        taskManager.addEpic(newEpic);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 3, 2);
        newEpic.addSubtasksInEpic(3);
        taskManager.addSubtask(newSubtask);
        Optional<Task> t = taskManager.getTaskById(1);
        Optional<Epic> e = taskManager.getEpicById(2);
        Optional<Subtask> s = taskManager.getSubtaskById(3);
        Assertions.assertEquals(newTask, t.get());
        Assertions.assertEquals(newEpic, e.get());
        Assertions.assertEquals(newSubtask, s.get());
    }

    @Test
    void calculationOfTheEpicStatusIfAllTheSubtasksStatusNew() {
            Epic e = new Epic("Name e", "Desr e", 1);
            taskManager.addEpic(e);
            Subtask s = new Subtask("Name s", "d1 s", Status.NEW, 2, 1);
            Subtask s1 = new Subtask("Name s1", "d1 s1", Status.NEW, 3, 1);
            taskManager.addSubtask(s);
            taskManager.addSubtask(s1);
            e.addSubtasksInEpic(2);
            e.addSubtasksInEpic(3);
            Assertions.assertEquals(Status.NEW, e.getStatus());
        }
    @Test
    void calculationOfTheEpicStatusIfAllTheSubtasksStatusDone() {
        Epic e = new Epic("Name e", "Desr e", 1);
        taskManager.addEpic(e);
        Subtask s = new Subtask("Name s", "d1 s", Status.DONE, 2, 1);
        Subtask s1 = new Subtask("Name s1", "d1 s1", Status.DONE, 3, 1);
        taskManager.addSubtask(s);
        taskManager.addSubtask(s1);
        e.addSubtasksInEpic(2);
        e.addSubtasksInEpic(3);
        Assertions.assertEquals(Status.DONE, e.getStatus());
    }

    @Test
    void calculationOfTheEpicStatusIfAllTheSubtasksStatusNewAndDone() {
        Epic e = new Epic("Name e", "Desr e", 1);
        taskManager.addEpic(e);
        Subtask s = new Subtask("Name s", "d1 s", Status.NEW, 2, 1);
        Subtask s1 = new Subtask("Name s1", "d1 s1", Status.DONE, 3, 1);
        taskManager.addSubtask(s);
        taskManager.addSubtask(s1);
        e.addSubtasksInEpic(2);
        e.addSubtasksInEpic(3);
        Assertions.assertNotEquals(Status.NEW, e.getStatus());
        Assertions.assertNotEquals(Status.DONE, e.getStatus());
    }

    @Test
    void calculationOfTheEpicStatusIfAllTheSubtasksStatusInProgress() {
        Epic e = new Epic("Name e", "Desr e", 1);
        taskManager.addEpic(e);
        Subtask s = new Subtask("Name s", "d1 s", Status.IN_PROGRESS, 2, 1);
        Subtask s1 = new Subtask("Name s1", "d1 s1", Status.IN_PROGRESS, 3, 1);
        taskManager.addSubtask(s);
        taskManager.addSubtask(s1);
        e.addSubtasksInEpic(2);
        e.addSubtasksInEpic(3);
        Assertions.assertEquals(Status.IN_PROGRESS, e.getStatus());
    }

}


