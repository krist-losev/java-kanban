package manager;

import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tasks.TypeTask.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public static void main(String[] args) {
        File newFile = new File("resources/task.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), newFile);

        Task task0 = new Task("Съесть сыр", "Пообедать", Status.NEW,
                LocalDateTime.of(2024, 5, 3, 12, 30), Duration.ofMinutes(10), 4);
        Task task = new Task("Искупаться", "Жариться в адском душе", Status.DONE,
                LocalDateTime.of(2024, 5, 1, 16, 3), Duration.ofMinutes(15), 5);
        Subtask subtask0 = new Subtask("Дописать программу", "писааать", Status.DONE, 6,
                LocalDateTime.of(2024, 5, 10, 20, 20), Duration.ofMinutes(60), 1);
        Subtask subtask1 = new Subtask("Проверить код", "Пропустить через дебагер", Status.IN_PROGRESS, 7,
                LocalDateTime.of(2024, 5, 4, 20, 20), Duration.ofMinutes(60),1);
        Subtask subtask2 = new Subtask("Собрать обед на работу", "Обеееед", Status.DONE, 8,
                LocalDateTime.of(2027, 5, 7, 20, 22), Duration.ofMinutes(15),2);
        Subtask subtask3 = new Subtask("Дописать программу", "писааать", Status.DONE, 9,
                LocalDateTime.of(2024, 5,9, 12, 0), Duration.ofMinutes(15), 1);

        List<Subtask> s1 = new ArrayList<>();
        s1.add(subtask0);
        s1.add(subtask1);
        s1.add(subtask3);

        List<Subtask> s2 = new ArrayList<>();
        s2.add(subtask2);

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

        fileBackedTaskManager.getTaskById(task.getIdNumber());
        fileBackedTaskManager.getEpicById(epic1.getIdNumber());
        fileBackedTaskManager.getSubtaskById(subtask1.getIdNumber());
        fileBackedTaskManager.getTaskById(task.getIdNumber());
        fileBackedTaskManager.getTaskById(task0.getIdNumber());
        System.out.println("История запросов: " + fileBackedTaskManager.getHistory());

        FileBackedTaskManager newFileBM = FileBackedTaskManager.loadFromFile(newFile);
        System.out.println("История запросов: " + newFileBM.getHistory());
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        try {
            String line = Files.readString(file.toPath());
            List<String> string = List.of(line.split("\n"));
            List<String> fileString =  new ArrayList<>(string);
            fileString.remove("");
            for (int i = 1; i < fileString.size(); i++) {
                if (i < fileString.size() - 2) {
                    Task task = fromString(fileString.get(i++));
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
                    List<Integer> history = historyFromString(fileString.getLast());
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
    public Optional<Task> getTaskById(int id) {
        Optional<Task> task = super.getTaskById(id);
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
    public Optional<Epic> getEpicById(int id) {
        Optional<Epic> epic = super.getEpicById(id);
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
    public Optional<Subtask> getSubtaskById(int id) {
        Optional<Subtask> subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    private void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id, type, name, status, description, startTime, duration, epic\n");
            for (Task task : listTask()) {
                fileWriter.write(taskToString(task, TASK));
            }
            for (Epic epic : listEpic()) {
                fileWriter.write(taskToString(epic, EPIC));
            }
            for (Subtask subtask : listSubtask()) {
                fileWriter.write(taskToString(subtask, SUBTASK));
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл!");
        }
    }

    private String taskToString(Task task, TypeTask typeTask) {
        String taskString = null;
        String startTime = null;
        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(FORMATTER);
        }
        Long durationTask = null;
        if (task.getDuration() != null) {
            durationTask = task.getDuration().toMinutes();
        }
        if (typeTask == TASK) {
            taskString = String.format("%s,%s,%s,%s,%s,%s,%s\n", task.getIdNumber(), TASK, task.getNameTask(),
                    task.getStatus(), task.getDescription(), startTime, durationTask);
        } else if (typeTask == EPIC) {
            Epic epic = (Epic) task;
            taskString = String.format("%s,%s,%s,%s,%s,%s,%s\n", epic.getIdNumber(), EPIC, epic.getNameTask(),
                    epic.getStatus(), epic.getDescription(), startTime,
                    durationTask);
        } else if (typeTask == SUBTASK) {
            Subtask subtask = (Subtask) task;
            taskString = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", subtask.getIdNumber(), SUBTASK,
                    subtask.getNameTask(), subtask.getStatus(), subtask.getDescription(),
                    startTime, durationTask, subtask.getIdEpic());
        }
        return taskString;
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TypeTask type = TypeTask.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        String start = parts[5];
        LocalDateTime startTime = null;
        if (!start.equals("null")) {
            startTime = LocalDateTime.parse(start, FORMATTER);
        }
        String durationTask = parts[6];
        Duration duration = null;
        if (!durationTask.equals("null")) {
            duration = Duration.ofMinutes(Long.parseLong(durationTask));
        }
        Task taskFromString = null;
        if (type == EPIC) {
            taskFromString = new Epic(name, description, id);
            taskFromString.setStatus(status);
        } else if (type == SUBTASK) {
            int epic = Integer.parseInt(parts[7]);
            taskFromString = new Subtask(name, description, status, id, startTime, duration, epic);
        } else if (type == TASK) {
            taskFromString = new Task(name, description, status, startTime, duration, id);
        }
        return taskFromString;
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
        for (int i = 0; i < part.length; i++) {
            int id = Integer.parseInt(part[i]);
            historyId.add(id);
        }
        return historyId;
    }
}