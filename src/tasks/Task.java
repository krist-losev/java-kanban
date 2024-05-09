package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private int idNumber;
    private String nameTask;
    private String description;
    private Status status;
    private TypeTask typeTask;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status, int idNumber) {
        this.nameTask = name;
        this.description = description;
        this.status = status;
        this.idNumber = idNumber;
    }

    public Task(String name, String description, Status status) {
        this.nameTask = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime,
                Duration duration) {
        this.nameTask = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, LocalDateTime startTime,
                Duration duration, int idNumber) {
        this.nameTask = name;
        this.description = description;
        this.status = status;
        this.idNumber = idNumber;
        this.duration = duration;
        this.startTime = startTime;
    }


    public LocalDateTime getEndTime() {
        LocalDateTime endTime;
        if (startTime == null) {
            endTime = null;
        } else {
            endTime = startTime.plusMinutes(duration.toMinutes());
        }
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return idNumber == task.idNumber && Objects.equals(nameTask, task.nameTask) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber, nameTask, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "idNumber=" + idNumber +
                ", nameTask='" + nameTask + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getTypeTask() {
        return TypeTask.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

}




