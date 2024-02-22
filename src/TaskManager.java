import java.util.HashMap;

public class TaskManager {

    private int idTask = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public int generateId() {
        return idTask++;
    }

    public Task addTask(Task newTask) { //создание задачи
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        return newTask;
    }

    public void updateTask(Task updateTask) { //обновление задачи
        Task currentTask = tasks.get(updateTask.getIdNumber());
        tasks.put(updateTask.getIdNumber(), currentTask);

    }

    public void deletedTaskId(int id) { //Удаление по идентификатору
        Task task = tasks.get(id);
        tasks.remove(task);

    }

    public void clearTask() {  //Удаление всех задач
        tasks.clear();
    }

    public void listTask() {
        System.out.println("Список задач: " + tasks.values());
    }//Получение списка всех задач

   public Task getTaskById(int id) {
       if (!tasks.containsKey(id)) {
           System.out.println("Задачи с таким номером нет!");
           return null;
       }
        Task task = tasks.get(id);// Получение по идентификатору
        return task;
    }

    public Epic addEpic (Epic newEpic) { //создание задачи
        int newEpickId = generateId();
        newEpic.setIdNumber(newEpickId);
        epics.put(newEpickId, newEpic);
        updateStatus(newEpic);
        return newEpic;
    }

    public void updateEpic(Epic updateEpic) { //обновление задачи
        Epic currentEpic = epics.get(updateEpic.getIdNumber());
        epics.put(updateEpic.getIdNumber(), currentEpic);
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

    public void listEpic() {
        System.out.println("Список эпиков: " + epics.values());
    }//Получение списка всех задач

   public Epic getEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпика с таким номером нет!");
            return null;
        }
        Epic epic = epics.get(id);// Получение по идентификатору
        return epic;
    }


    public Subtask addSubtask(Subtask subtask) {
        int newSubtaskId = generateId();
        subtask.setIdNumber(newSubtaskId);
        subtasks.put(newSubtaskId,  subtask);
        epics.get(subtask.getIdEpic()).addSubtasksInEpic(subtask.getIdNumber());
        updateStatus(epics.get(subtask.getIdEpic()));
        return  subtask;

    }
    public Subtask updateSubtask(Subtask updateSubtask) {
        Subtask currentSubtask = subtasks.get(updateSubtask.getIdNumber());
        subtasks.put(updateSubtask.getIdNumber(), currentSubtask);
        updateStatus(epics.get(updateSubtask.getIdEpic()));
        return updateSubtask;
    }
    public void deletedSubtaskId (int id) { //Удаление по идентификатору
        Subtask subtask = subtasks.get(id);
        epics.get(subtask.getIdEpic()).getSubtasksId().remove(id);
        subtasks.remove(id);
        updateStatus(epics.get(subtask.getIdEpic()));

    }
    public Subtask deleteSubtask(Subtask subtask) {  //Удаление всех задач
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateStatus(epics.get(subtask.getIdEpic()));
        }
        return subtask;
    }
    public void listSubtask() {
        System.out.println("Список подзадач: " + subtasks.values());
    }//Получение списка всех задач
    public Subtask getSubtaskById(int id){
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадачи с таким номером нет!");
            return null;
        }
        Subtask subtask = subtasks.get(id);// Получение по идентификатору
        return subtask;
    }

    public void updateStatus(Epic epic) {
        for (Integer subtasksId : epic.getSubtasksId()) {
            if (subtasks.get(subtasksId).getStatus() != Status.DONE) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else {
                epic.setStatus(Status.DONE);
            }
        }
    }
    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }
}



