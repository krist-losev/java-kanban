import manager.FileBackedTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FBMtest {
    FileBackedTaskManager manager;
    File newFile;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), newFile);
        newFile = File.createTempFile("test", ".csv");
    }
    //сохранение и загрузку пустого файла;
    //сохранение нескольких задач;
    //загрузку нескольких задач.

    @Test
    void savingAndUploadingAnEmptyFile() {
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(newFile);
        assertTrue(manager2.getHistory().isEmpty(), "История пуста");
    }

}
