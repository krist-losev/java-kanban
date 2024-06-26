package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {

    private List<Integer> subtasksId;
    private LocalDateTime endTime;

    public Epic(String nameTask, String description, int idNumber) {
        super(nameTask, description, Status.NEW, idNumber);
        this.subtasksId = new ArrayList<>();
    }

    public void addSubtasksInEpic(int idSubtask) {
        subtasksId.add(idSubtask);
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtask() {
        subtasksId.clear();
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}






