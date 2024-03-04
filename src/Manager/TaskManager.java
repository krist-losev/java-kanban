package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task addTask(Task newTask);

    void updateTask(Task updateTask);

    void deleteTaskById(int id);

    void clearTask();

    ArrayList<Task> listTask();//Получение списка всех задач


    Task getTaskById(int id);

    Epic addEpic(Epic newEpic);

    void updateEpic(Epic updateEpic);

    void deleteEpicById(int id);

    void clearEpic();

    ArrayList<Epic> listEpic();//Получение списка всех задач


    Epic getEpicById(int id);

    Subtask addSubtask(Subtask subtask);

    void updateSubtask(Subtask updateSubtask);

    void deleteSubtaskById(int id);

    void deleteSubtask();

    ArrayList<Subtask> listSubtask();

    Subtask getSubtaskById(int id);

    List<Task> getHistory();
}
