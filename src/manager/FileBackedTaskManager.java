package manager;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static tasks.TypeTask.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        List<Integer> history;
        try {
            String line = Files.readString(file.toPath());
            List<String> string = List.of(line.split("\n"));
            List <String> fileString =  new ArrayList<>(string);
            fileString.remove("");
            for (int i = 1; i < fileString.size(); i++) {
                if (i < fileString.size() - 2) {
                    Task task = fromString(fileString.get(i + 1));
                    switch (task.getTypeTask()) {
                        case TASK:
                            fileBackedTaskManager.tasks.put(task.getIdNumber(), task);
                            break;
                        case EPIC:
                            Epic epic = (Epic) task;
                            fileBackedTaskManager.epics.put(epic.getIdNumber(), epic);
                            break;
                        case SUBTASK:
                            Subtask subtask = (Subtask) task;
                            fileBackedTaskManager.subtasks.put(subtask.getIdNumber(), subtask);
                            Epic epicWithASubtask = fileBackedTaskManager.epics.get(subtask.getIdEpic());
                            epicWithASubtask.addSubtasksInEpic(subtask.getIdNumber());
                            break;
                    }
                } else {
                    history = historyFromString(fileString.get(fileString.size() - 1));
                    HistoryManager historyManager = fileBackedTaskManager.historyManager;
                    for (Integer id : history) {
                        if (fileBackedTaskManager.tasks.containsKey(id)) {
                            historyManager.add(fileBackedTaskManager.tasks.get(id));
                        } else if (fileBackedTaskManager.epics.containsKey(id)) {
                            historyManager.add(fileBackedTaskManager.epics.get(id));
                        } else if (fileBackedTaskManager.subtasks.containsKey(id)) {
                            historyManager.add(fileBackedTaskManager.subtasks.get(id));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id, type, name, status, description, epic\n");
            for (Task task : listTask()) {
                fileWriter.write(taskToString(task));
            }
            for (Epic epic : listEpic()) {
                fileWriter.write(epicToString(epic));
            }
            for (Subtask subtask : listSubtask()) {
                fileWriter.write(subtaskToString(subtask));
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл!");
        }
    }

    private String taskToString (Task task) {
        return String.format("%s,%s,%s,%s,%s,\n", task.getIdNumber(), TASK, task.getNameTask(),
                task.getStatus(), task.getDescription());
    }

    private String epicToString(Epic epic) {
        return String.format("%s,%s,%s,%s,%s,\n", epic.getIdNumber(), EPIC, epic.getNameTask(),
                epic.getStatus(), epic.getDescription());
    }

    private String subtaskToString(Subtask subtask) {
        return String.format("%s,%s,%s,%s,%s,%s,\n", subtask.getIdNumber(), SUBTASK,
                subtask.getNameTask(), subtask.getStatus(), subtask.getDescription(), subtask.getIdEpic());
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TypeTask type = TypeTask.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        if (type == EPIC) {
            Epic epicString = new Epic(name, description, id);
            epicString.setStatus(status);
            return epicString;
        } else if (type == SUBTASK) {
            int epic = Integer.parseInt(parts[5]);
            Subtask subtaskString = new Subtask(name, description, status, id, epic);
            return subtaskString;
        } else if (type == TASK) {
            Task taskString = new Task(name, description, status, id);
            return taskString;
        }
        return null;

    }

    private String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        String[] stringsHistory = new String[history.size()];
        for (int i = 0; i < stringsHistory.length; i++) {
            stringsHistory[i] = String.valueOf(history.get(i).getIdNumber());
        }
        return String.join(",", stringsHistory);
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> historyId = new ArrayList<>();
        String[] part = value.split(",");
        for (String string : part) {
            historyId.add(Integer.parseInt(string));
        }
        return historyId;
    }

    @Override
    public Task addTask(Task newTask) {
        Task task = super.addTask(newTask);
        save();
        return task;
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        Epic epic = super.addEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask newSubtask) {
        Subtask subtask = super.addSubtask(newSubtask);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        super.updateSubtask(updateSubtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    public static void main(String[] args) {
        File newFile = new File("resources/task.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), newFile);

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

        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addEpic(epic2);
        fileBackedTaskManager.addEpic(epic3);

        fileBackedTaskManager.addTask(task0);
        fileBackedTaskManager.addTask(task);

        fileBackedTaskManager.addSubtask(subtask0);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.addSubtask(subtask2);
        fileBackedTaskManager.addSubtask(subtask3);
        System.out.println("Список задач: " + fileBackedTaskManager.listTask());
        System.out.println("Список эпиков: " + fileBackedTaskManager.listEpic());
        System.out.println("Список подзадач: " + fileBackedTaskManager.listSubtask());

        fileBackedTaskManager.getTaskById(4);
        fileBackedTaskManager.getEpicById(1);
        fileBackedTaskManager.getSubtaskById(7);
        fileBackedTaskManager.getTaskById(4);
        fileBackedTaskManager.getTaskById(5);
        fileBackedTaskManager.getSubtaskById(9);
        System.out.println("История запросов: " + fileBackedTaskManager.getHistory());

        FileBackedTaskManager newFileBM = FileBackedTaskManager.loadFromFile(newFile);
        System.out.println(newFileBM.getHistory());
    }

}