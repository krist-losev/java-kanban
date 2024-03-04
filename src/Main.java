import Manager.HistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Manager.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager inMemoryTaskManager = Managers.getDefault();


        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.

        Task task0 = new Task("Съесть сыр", "Пообедать", Status.NEW, 3);
        Task task = new Task("Искупаться", "Жариться в адском душе", Status.DONE, 4);

        Subtask subtask0 = new Subtask("Дописать программу", "писааать", Status.DONE, 5, 1);
        Subtask subtask1 = new Subtask("Проверить код", "Пропустить через дебагер", Status.IN_PROGRESS, 6, 1);
        Subtask subtask2 = new Subtask("Собрать обед на работу", "Обеееед", Status.DONE, 7, 2);
        Subtask subtask3 = new Subtask("Дописать программу", "писааать", Status.DONE, 8, 1);

        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask0);
        subtasks1.add(subtask1);
        subtasks1.add(subtask3);

        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask2);


        Epic epic1 = new Epic("Сдать тз4", "Доделать дз и сдать его", 1);
        Epic epic2 = new Epic("Подготовиться на работу", "Опять вставать в пять утра:(", 2);
        Epic epic3 = new Epic("Починить машину", "Срочно", 3);

        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addEpic(epic3);

        inMemoryTaskManager.addTask(task0);
        inMemoryTaskManager.addTask(task);

        inMemoryTaskManager.addSubtask(subtask0);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);


        //Распечатайте списки эпиков, задач и подзадач

        System.out.println("Список задач: " + inMemoryTaskManager.listTask());
        System.out.println("Список эпиков: " + inMemoryTaskManager.listEpic());
        System.out.println("Список подзадач: " + inMemoryTaskManager.listSubtask());



        //удалить одну из задач
        inMemoryTaskManager.deleteTaskById(4);
        System.out.println("Список задач: " + inMemoryTaskManager.listTask());


        //удалить один из эпиков
        inMemoryTaskManager.deleteEpicById(2);
        System.out.println("Список эпиков: " + inMemoryTaskManager.listEpic());

        inMemoryTaskManager.getSubtaskById(6);
        List<Task> hh = inMemoryTaskManager.getHistory();
        System.out.println("История: " + hh);
        inMemoryTaskManager.getEpicById(3);
        List<Task> hhh = inMemoryTaskManager.getHistory();
        System.out.println("История: " + hhh);
    }
}
