package tasks;

import java.util.Objects;

public class Task {

    private int idNumber;
    private String nameTask;
    private String description;
    private Status status;

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
}




