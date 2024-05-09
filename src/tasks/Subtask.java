package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int idEpic;

    public Subtask(String name, String description, Status status, int idNumber, int idEpic) {
        super(name, description, status, idNumber);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, Status status, int idNumber, LocalDateTime startTime,
                   Duration duration, int idEpic) {
        super(name, description, status, startTime, duration, idNumber);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, Status status, LocalDateTime startTime,
                   Duration duration, int idEpic) {
        super(name, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }
}





