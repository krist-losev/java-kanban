import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {

    public static void main (String[] args) {
        /* Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
Запросите созданные задачи несколько раз в разном порядке.
После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.*/
        TaskManager taskManager= Managers.getDefault();

        Task task0 = new Task("Съесть сыр", "Пообедать", Status.NEW);
        Task task = new Task("Искупаться", "Жариться в адском душе", Status.DONE);

        Subtask subtask0 = new Subtask("Дописать программу", "писааать", Status.DONE, 6, 1);
        Subtask subtask1 = new Subtask("Проверить код", "Пропустить через дебагер", Status.IN_PROGRESS, 7, 1);
        Subtask subtask2 = new Subtask("Собрать обед на работу", "Обеееед", Status.DONE, 8, 2);
        Subtask subtask3 = new Subtask("Дописать программу", "писааать", Status.DONE, 9, 1);

        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask0);
        subtasks1.add(subtask1);
        subtasks1.add(subtask3);

        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask2);

        Epic epic1 = new Epic("Сдать тз4", "Доделать дз и сдать его", 1);
        Epic epic2 = new Epic("Подготовиться на работу", "Опять вставать в пять утра:(", 2);
        Epic epic3 = new Epic("Починить машину", "Срочно", 3);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        taskManager.addTask(task0);
        taskManager.addTask(task);

        taskManager.addSubtask(subtask0);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        System.out.println("Список задач: " + taskManager.listTask());
        System.out.println("Список эпиков: " + taskManager.listEpic());
        System.out.println("Список подзадач: " + taskManager.listSubtask());

        taskManager.getTaskById(4);
        System.out.println("История запросов: " + taskManager.getHistory());
        taskManager.getEpicById(1);
        System.out.println("История запросов: " + taskManager.getHistory());
        taskManager.getSubtaskById(7);
        taskManager.getTaskById(4);
        System.out.println("История запросов: " + taskManager.getHistory());
    }
}
