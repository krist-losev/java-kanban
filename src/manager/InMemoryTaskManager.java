package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    private int idTask = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder()));
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    public HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    //создание задач
    @Override
    public Task addTask(Task newTask) {
        if (timeIsCrossing(newTask)) {
            System.out.println("Время задач пересекается. Пожалуйста, выберите другое время.");
            return null;
        }
        int newTaskId = generateId();
        newTask.setIdNumber(newTaskId);
        tasks.put(newTaskId, newTask);
        prioritizedTasks.add(newTask);
        return newTask;
    }

    //обновление задачи
    @Override
    public void updateTask(Task updateTask) { //удалить из приоритета старый и добавить новый
        tasks.put(updateTask.getIdNumber(), updateTask);
        prioritizedTasks.removeIf(oldTask -> oldTask.getIdNumber() == updateTask.getIdNumber());
        prioritizedTasks.add(updateTask);
    }

    //Удаление по идентификатору
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
    }

    //Удаление всех задач
    @Override
    public void clearTask() {
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getTypeTask().equals(TypeTask.TASK));
    }

    //Получение списка всех задач
    @Override
    public List<Task> listTask() {
        return new ArrayList<>(tasks.values());
    }

   @Override
   public Optional<Task> getTaskById(int id) {
       Optional<Task> taskById = Optional.of(tasks.get(id));
       if (taskById.isEmpty()) {
           System.out.println("Задачи с таким номером нет!");
           return Optional.empty();
       }
       historyManager.add(taskById.get());
       return taskById;
    }

    //создание задачи
    @Override
    public Epic addEpic(Epic newEpic) {
        int newEpicId = generateId();
        newEpic.setIdNumber(newEpicId);
        epics.put(newEpicId, newEpic);
        updateStatus(newEpic);
        timeCounting(newEpic);
        prioritizedTasks.add(newEpic);
        return newEpic;
    }

    //обновление задачи
    @Override
    public void updateEpic(Epic updateEpic) { //удалить из приоритета старый и добавить новый
        epics.put(updateEpic.getIdNumber(), updateEpic);
        prioritizedTasks.removeIf(oldEpic -> oldEpic.getIdNumber() == updateEpic.getIdNumber());
        prioritizedTasks.add(updateEpic);
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
        prioritizedTasks.remove(epic);
    }

    //Удаление всех задач
    @Override
    public void clearEpic() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(epic -> epic.getTypeTask().equals(TypeTask.EPIC));
    }

    //Получение списка всех задач
    @Override
    public List<Epic> listEpic() {
        return new ArrayList<>(epics.values());
    }

   @Override
   public Optional<Epic> getEpicById(int id) {
       Optional<Epic> epicById = Optional.of(epics.get(id));
       if (epicById.isEmpty()) {
           System.out.println("Эпика с таким номером нет!");
           return Optional.empty();
       }
       historyManager.add(epicById.get());
       return epicById;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (timeIsCrossing(subtask)) {
            System.out.println("Время задач пересекается. Пожалуйста, выберите другое время.");
            return null;
        }
        int newSubtaskId = generateId();
        subtask.setIdNumber(newSubtaskId);
        subtasks.put(newSubtaskId, subtask);
        Epic nEpic = epics.get(subtask.getIdEpic());
        nEpic.addSubtasksInEpic(subtask.getIdNumber());
        timeCounting(nEpic);
        updateStatus(nEpic);
        prioritizedTasks.add(subtask);
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask updateSubtask) {
        subtasks.put(updateSubtask.getIdNumber(), updateSubtask);
        updateEpic(epics.get(updateSubtask.getIdNumber()));
        updateStatus(epics.get(updateSubtask.getIdEpic()));
        timeCounting(epics.get(updateSubtask.getIdEpic()));
        prioritizedTasks.removeIf(oldSubtask -> oldSubtask.getIdNumber() == updateSubtask.getIdNumber());
        prioritizedTasks.add(updateSubtask);
    }

    //Удаление по идентификатору
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getIdEpic());
        subtasks.remove(id);
        epic.getSubtasksId().remove(subtask);
        historyManager.remove(id);
        prioritizedTasks.remove(subtask);
        updateStatus(epics.get(subtask.getIdEpic()));
        timeCounting(epics.get(subtask.getIdEpic()));
    }

    //Удаление всех задач
    @Override
    public void deleteSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtask();
            updateStatus(epic);
            timeCounting(epic);
        }
        prioritizedTasks.removeIf(subtask -> subtask.getTypeTask().equals(TypeTask.SUBTASK));
    }

    //Получение списка всех задач
    @Override
    public List<Subtask> listSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    // Получение по идентификатору
    @Override
    public Optional<Subtask> getSubtaskById(int id) {
        Optional<Subtask> subtaskById = Optional.of(subtasks.get(id));
        if (subtaskById.isEmpty()) {
            System.out.println("Подзадачи с таким номером нет!");
            return Optional.empty();
        }
        historyManager.add(subtaskById.get());
        return subtaskById;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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

    //метод для подсчета времени старта эпика и его продолжительность
        private void timeCounting(Epic epic) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = null;
        List<Subtask> subtaskInEpic = new ArrayList<>();
        if (!epic.getSubtasksId().isEmpty()) {
            for (Integer sub : epic.getSubtasksId()) {
                subtaskInEpic.add(subtasks.get(sub));
            }
            subtaskInEpic.sort(taskComparator);
            if (subtaskInEpic.getFirst().getStartTime() != null) {
                startTime = subtaskInEpic.getFirst().getStartTime();
                endTime = subtaskInEpic.getLast().getEndTime();
                duration = Duration.between(startTime, endTime);
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    //метод определения пересечения задач по времени
    private boolean timeIsCrossing(Task newTask) {
        boolean crossing = false;
        if (prioritizedTasks.isEmpty()) {
            return crossing;
        }
        LocalDateTime begin = newTask.getStartTime();
        LocalDateTime end = newTask.getEndTime();
        if (begin != null) {
            for (Task oldTask : prioritizedTasks) {
                LocalDateTime start = oldTask.getStartTime();
                LocalDateTime finish = oldTask.getEndTime();
                if (start == null) {
                    crossing = false;
                } else {
                    if (begin.isEqual(start) || end.isEqual(finish) || begin.isEqual(finish) || end.isEqual(start)) {
                        crossing = true;
                    }
                    if (begin.isBefore(finish) && begin.isAfter(start)) {
                        crossing = true;
                    }
                    if (end.isAfter(start) && end.isBefore(finish)) {
                        crossing = true;
                    }
                }
            }
        }
        return crossing;
    }

    private int generateId() {
        return idTask++;
    }

}



