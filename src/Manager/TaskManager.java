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



    public Task addTask(Task newTask) { //создание задачи
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        return newTask;
    }

    public void updateTask(Task updateTask) { //обновление задачи
        tasks.put(updateTask.getIdNumber(), updateTask);
    }

    public void deleteTaskById(int id) { //Удаление по идентификатору

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

    public void updateEpic(Epic updateEpic) { //обновление задачи
        epics.put(updateEpic.getIdNumber(), updateEpic);

    }

    public void deleteEpicById(int id) {
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
        subtasks.put(updateSubtask.getIdNumber(), updateSubtask);
        updateEpic(epics.get(updateSubtask.getIdNumber()));
        updateStatus(epics.get(updateSubtask.getIdEpic()));

    }
    public void deleteSubtaskById (int id) { //Удаление по идентификатору
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



