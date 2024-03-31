package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {

    private int idTask = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private  HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    //создание задачи
    @Override
    public Task addTask(Task newTask) {
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        return newTask;
    }

    //обновление задачи
    @Override
    public void updateTask(Task updateTask) {
        tasks.put(updateTask.getIdNumber(), updateTask);
    }

    //Удаление по идентификатору
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    //Удаление всех задач
    @Override
    public void clearTask() {
        tasks.clear();
    }

    //Получение списка всех задач
    @Override
    public List<Task> listTask() {
        return new ArrayList<Task>(tasks.values());
    }

   @Override
   public Task getTaskById(int id) {
       if (!tasks.containsKey(id)) {
           System.out.println("Задачи с таким номером нет!");
           return null;
       }
       Task task = tasks.get(id);
       historyManager.add(task);
       return task;
    }

    //создание задачи
    @Override
    public Epic addEpic(Epic newEpic) {
        int newEpickId = generateId();
        newEpic.setIdNumber(newEpickId);
        epics.put(newEpickId, newEpic);
        updateStatus(newEpic);
        return newEpic;
    }

    //обновление задачи
    @Override
    public void updateEpic(Epic updateEpic) {
        epics.put(updateEpic.getIdNumber(), updateEpic);

    }

    //Удаление по идентификатору
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtasksId()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
        historyManager.remove(id);

    }

    //Удаление всех задач
    @Override
    public void clearEpic() {
        epics.clear();
        subtasks.clear();
    }

    //Получение списка всех задач
    @Override
    public List<Epic> listEpic() {
        return new ArrayList<Epic>(epics.values());
    }

   @Override
   public Epic getEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким номером нет!");
            return null;
        }
       Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }


    @Override
    public Subtask addSubtask(Subtask subtask) {
        int newSubtaskId = generateId();
        subtask.setIdNumber(newSubtaskId);
        subtasks.put(newSubtaskId, subtask);
        Epic nEpic = epics.get(subtask.getIdEpic());
        nEpic.addSubtasksInEpic(subtask.getIdNumber());
        updateStatus(nEpic);
        return  subtask;
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        subtasks.put(updateSubtask.getIdNumber(), updateSubtask);
        updateEpic(epics.get(updateSubtask.getIdNumber()));
        updateStatus(epics.get(updateSubtask.getIdEpic()));
    }

    //Удаление по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        epics.get(subtask.getIdEpic()).getSubtasksId().remove(id);
        subtasks.remove(id);
        historyManager.remove(id);
        updateStatus(epics.get(subtask.getIdEpic()));
    }

    //Удаление всех задач
    @Override
    public void deleteSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateStatus(epic);
        }
    }

    //Получение списка всех задач
    @Override
    public List<Subtask> listSubtask() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    // Получение по идентификатору
    @Override
    public Subtask getSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадачи с таким номером нет!");
            return null;
        }
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateStatus(Epic epic) {
        boolean allStatusDone = true;
        boolean allStatusNew = true;
        for (Integer subtasksId : epic.getSubtasksId()) {
            if (subtasks.get(subtasksId).getStatus() != Status.DONE) {
                allStatusDone = false;
            }
            if (subtasks.get(subtasksId).getStatus() != Status.NEW) {
                allStatusNew = false;
            }
        }
        if (allStatusNew) {
            epic.setStatus(Status.NEW);
        } else if (allStatusDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private int generateId() {
        return idTask++;
    }

}



