package Manager;

import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;


import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {

    private int idTask = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public int generateId() {
        return idTask++;
    }

    public Task addTask(Task newTask) { //создание задачи
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        return newTask;
    }

    public Task updateTask(Task updateTask) { //обновление задачи
        Task currentTask = tasks.get(updateTask.getIdNumber());
        currentTask.setNameTask(updateTask.getNameTask());
        currentTask.setDescription(updateTask.getDescription());
        currentTask.setStatus(updateTask.getStatus());
        return currentTask;
    }

    public void deletedTaskId(int id) { //Удаление по идентификатору

        tasks.remove(id);

    }

    public void clearTask() {  //Удаление всех задач
        tasks.clear();
    }

    public ArrayList<Task> listTask() {
        return new ArrayList<Task>(tasks.values());
    }//Получение списка всех задач

   public Task getTaskById(int id) {
       if (!tasks.containsKey(id)) {
           System.out.println("Задачи с таким номером нет!");
           return null;
       }
        return tasks.get(id);
    }

    public Epic addEpic (Epic newEpic) { //создание задачи
        int newEpickId = generateId();
        newEpic.setIdNumber(newEpickId);
        epics.put(newEpickId, newEpic);
        updateStatus(newEpic);
        return newEpic;
    }

    public Epic updateEpic(Epic updateEpic) { //обновление задачи
        Epic currentEpic = epics.get(updateEpic.getIdNumber());
        currentEpic.setNameTask(updateEpic.getNameTask());
        currentEpic.setDescription(updateEpic.getDescription());
        return currentEpic;
    }

    public void deletedEpicId(int id) {
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtasksId()) {
            subtasks.remove(idSubtask);//Удаление по идентификатору
        }
        epics.remove(id);

    }

    public void clearEpic() {  //Удаление всех задач
        epics.clear();
        subtasks.clear();
    }

    public ArrayList<Epic> listEpic() {

        return new ArrayList<Epic>(epics.values());
    }//Получение списка всех задач

   public Epic getEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким номером нет!");
            return null;
        }
        return epics.get(id);
    }


    public Subtask addSubtask(Subtask subtask) {
        int newSubtaskId = generateId();
        subtask.setIdNumber(newSubtaskId);
        subtasks.put(newSubtaskId, subtask);
        Epic nEpic = epics.get(subtask.getIdEpic());
        nEpic.addSubtasksInEpic(subtask.getIdNumber());
        updateStatus(nEpic);
        return  subtask;

    }
    public void updateSubtask(Subtask updateSubtask) {
        Subtask currentSubtask = subtasks.get(updateSubtask.getIdNumber());
        currentSubtask.setNameTask(updateSubtask.getNameTask());
        currentSubtask.setDescription(updateSubtask.getDescription());
        currentSubtask.setStatus(updateSubtask.getStatus());
        updateEpic(epics.get(currentSubtask.getIdNumber()));
        updateStatus(epics.get(updateSubtask.getIdEpic()));

    }
    public void deletedSubtaskId (int id) { //Удаление по идентификатору
        Subtask subtask = subtasks.get(id);
        epics.get(subtask.getIdEpic()).getSubtasksId().remove(id);
        subtasks.remove(id);
        updateStatus(epics.get(subtask.getIdEpic()));

    }
    public void deleteSubtask() {  //Удаление всех задач
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateStatus(epic);
        }

    }
    public ArrayList<Subtask> listSubtask() {//Получение списка всех задач
        return new ArrayList<Subtask>(subtasks.values());
    }
    public Subtask getSubtaskById(int id){
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадачи с таким номером нет!");
            return null;
        }
        Subtask subtask = subtasks.get(id);// Получение по идентификатору
        return subtask;
    }

    private void updateStatus(Epic epic) {
        for (Integer subtasksId : epic.getSubtasksId()) {
            if (subtasks.get(subtasksId).getStatus() != Status.IN_PROGRESS && subtasks.get(subtasksId).getStatus() != Status.DONE) {
                epic.setStatus(Status.NEW);
                return;
            } else if (subtasks.get(subtasksId).getStatus() != Status.DONE) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else {
                epic.setStatus(Status.DONE);
            }
        }
    }

}



