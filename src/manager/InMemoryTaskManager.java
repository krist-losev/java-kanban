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

    @Override
    public Task addTask(Task newTask) { //создание задачи
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        return newTask;
    }

    @Override
    public void updateTask(Task updateTask) { //обновление задачи
        tasks.put(updateTask.getIdNumber(), updateTask);
    }

    @Override
    public void deleteTaskById(int id) { //Удаление по идентификатору
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void clearTask() {  //Удаление всех задач
        tasks.clear();
    }

    @Override
    public List<Task> listTask() {
        return new ArrayList<Task>(tasks.values());
    } //Получение списка всех задач

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

    @Override
    public Epic addEpic(Epic newEpic) { //создание задачи
        int newEpickId = generateId();
        newEpic.setIdNumber(newEpickId);
        epics.put(newEpickId, newEpic);
        updateStatus(newEpic);
        return newEpic;
    }

    @Override
    public void updateEpic(Epic updateEpic) { //обновление задачи
        epics.put(updateEpic.getIdNumber(), updateEpic);

    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtasksId()) {
            subtasks.remove(idSubtask);//Удаление по идентификатору
        }
        epics.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void clearEpic() {  //Удаление всех задач
        epics.clear();
        subtasks.clear();
    }

    @Override
    public List<Epic> listEpic() {
        return new ArrayList<Epic>(epics.values());
    } //Получение списка всех задач

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

    @Override
    public void deleteSubtaskById(int id) { //Удаление по идентификатору
        Subtask subtask = subtasks.get(id);
        epics.get(subtask.getIdEpic()).getSubtasksId().remove(id);
        subtasks.remove(id);
        historyManager.remove(id);
        updateStatus(epics.get(subtask.getIdEpic()));
    }

    @Override
    public void deleteSubtask() {  //Удаление всех задач
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateStatus(epic);
        }
    }

    @Override
    public List<Subtask> listSubtask() { //Получение списка всех задач
        return new ArrayList<Subtask>(subtasks.values());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадачи с таким номером нет!");
            return null;
        }
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);// Получение по идентификатору
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



