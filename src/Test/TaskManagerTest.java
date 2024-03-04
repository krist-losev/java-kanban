package Test;

import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    void beforeEach () {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask_TasksWithTheSameIDsAreEqual() {
        Task expectedTask = new Task("Task1", "D1", Status.NEW, 1);
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Task actualTask = taskManager.getTaskById(1);
        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    void addEpic_EpicsWithTheSameIDsAreEqual () {
        Epic expectedEpic = new Epic("Ep2", "D1", 1);
        Epic newEpic = new Epic("Ep2", "D1", 1);
        taskManager.addEpic(newEpic);
        Epic actualEpic = taskManager.getEpicById(1);
        Assertions.assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void addSubtask_SubtasksWithTheSameIDsAreEqual () {
        Subtask expectedSubtask = new Subtask("St1", "D1", Status.NEW, 2, 1);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 2, 1);
        Epic nEpic = new Epic("E", "D", 1);
        taskManager.addEpic(nEpic);
        nEpic.addSubtasksInEpic(2);
        taskManager.addSubtask(newSubtask);
        Subtask actualSubtask = taskManager.getSubtaskById(2);
        Assertions.assertEquals(expectedSubtask, actualSubtask);
    }

    @Test
    void addTask_TheSpecifiedIDsAndTheGeneratedIDsDoNotConflict () {
        Task newTask1 = new Task("Task2", "D2", Status.NEW);
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        taskManager.addTask(newTask1);
        Assertions.assertNotEquals(newTask1, newTask);
}
    @Test
    void inMemoryTaskManagerAddTaskEpicSubtaskAndGet () {
        Task newTask = new Task("Task1", "D1", Status.NEW, 1);
        taskManager.addTask(newTask);
        Epic newEpic = new Epic("Ep2", "D1", 2);
        taskManager.addEpic(newEpic);
        Subtask newSubtask = new Subtask("St1", "D1", Status.NEW, 3, 2);
        newEpic.addSubtasksInEpic(3);
        taskManager.addSubtask(newSubtask);
        Assertions.assertEquals(newTask, taskManager.getTaskById(1));
        Assertions.assertEquals(newEpic, taskManager.getEpicById(2));
        Assertions.assertEquals(newSubtask, taskManager.getSubtaskById(3));
    }

    @Test
    void updateStatusEpicTest () {
        Epic newEpic = new Epic("Name1", "D2", 1);
        taskManager.addEpic(newEpic);
        Subtask subtask0 = new Subtask("NameS1", "D2", Status.NEW, 2, 1);
        Subtask subtask1 = new Subtask("NameS2", "L2", Status.IN_PROGRESS, 3, 1);
        taskManager.addSubtask(subtask0);
        taskManager.addSubtask(subtask1);
        newEpic.addSubtasksInEpic(2);
        newEpic.addSubtasksInEpic(3);
        Assertions.assertEquals(Status.IN_PROGRESS, newEpic.getStatus());
    }


}
