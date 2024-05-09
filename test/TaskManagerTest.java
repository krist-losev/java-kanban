import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

}
