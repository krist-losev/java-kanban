import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.

        Task task0 = new Task("Съесть сыр", "Пообедать", Status.NEW, 3);
        Task task = new Task("Искупаться", "Жариться в адском душе", Status.DONE, 4);

        Subtask subtask0 = new Subtask("Дописать программу", "писааать", Status.IN_PROGRESS, 5, 1);
        Subtask subtask1 = new Subtask("Проверить код", "Пропустить через дебагер", Status.NEW, 6, 1);
        Subtask subtask2 = new Subtask("Собрать обед на работу", "Обеееед", Status.DONE, 7, 2);

        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask0);
        subtasks1.add(subtask1);

        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask2);


        Epic epic1 = new Epic ("Сдать тз4", "Доделать дз и сдать его", 1);
        Epic epic2 = new Epic("Подготовиться на работу", "Опять вставать в пять утра:(", 2);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.addTask(task0);
        taskManager.addTask(task);

        taskManager.addSubtask(subtask0);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        //Распечатайте списки эпиков, задач и подзадач

        taskManager.listTask();
        taskManager.listEpic();
        taskManager.listSubtask();

        //удалить одну из задач
        taskManager.deletedTaskId(1);
        taskManager.listTask();


        //удалить один из эпиков
        taskManager.deletedEpicId(2);
        taskManager.listEpic();
    }
}
