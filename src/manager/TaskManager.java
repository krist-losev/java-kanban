package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    Task addTask(Task newTask);

    void updateTask(Task updateTask);

    void deleteTaskById(int id);

    void clearTask();

    List<Task> listTask();//Получение списка всех задач

    Optional<Task> getTaskById(int id);

    Epic addEpic(Epic newEpic);

    void updateEpic(Epic updateEpic);

    void deleteEpicById(int id);

    void clearEpic();

    List<Epic> listEpic();//Получение списка всех задач


    Optional<Epic> getEpicById(int id);

    Subtask addSubtask(Subtask subtask);

    void updateSubtask(Subtask updateSubtask);

    void deleteSubtaskById(int id);

    void deleteSubtask();

    List<Subtask> listSubtask();

    Optional<Subtask> getSubtaskById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
