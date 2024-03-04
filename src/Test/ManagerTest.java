package Test;

import Manager.Managers;
import Manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagerTest {

    @Test
    void getDefaultTaskManager () {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager);
    }
}
