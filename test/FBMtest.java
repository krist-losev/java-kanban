import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FBMtest {

    @Test
    void savingAndUploadingAnEmptyFile() throws IOException {
        File newFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), newFile);
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(newFile);
        assertTrue(manager2.getHistory().isEmpty(), "История пуста");
        assertTrue(manager2.listTask().isEmpty(), "Задач нет");
        assertTrue(manager2.listEpic().isEmpty(), "Эпиков нет");
        assertTrue(manager2.listSubtask().isEmpty(), "Подзадач нет");
    }

    @Test
    void savingAndUploadingMultipleTasks () throws IOException {
        File newFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), newFile);
        Task t1 = new Task("name t1", "desc t1", Status.NEW);
        Task t2 = new Task("name t2", "desc t2", Status.IN_PROGRESS);
        manager.addTask(t1);
        manager.addTask(t2);
        manager.getTaskById(t1.getIdNumber());
        manager.getTaskById(t2.getIdNumber());
        System.out.println(manager.getHistory());
        assertTrue(!manager.getHistory().isEmpty());

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(newFile);
        assertTrue(!manager2.getHistory().isEmpty());
        assertTrue(!manager2.listTask().isEmpty());
    }


}
