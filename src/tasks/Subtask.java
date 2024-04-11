package tasks;

public class Subtask extends Task {

    private int idEpic;

    public Subtask(String name, String description, Status status, int idNumber, int idEpic) {
        super(name, description, status, idNumber);
        this.idEpic = idEpic;
    }


    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }
}





