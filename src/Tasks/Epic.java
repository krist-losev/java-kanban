package Tasks;

import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> subtasksId;


    public Epic (String nameTask, String description, int idNumber) {
        super(nameTask, description, Status.NEW, idNumber);
        this.subtasksId = new ArrayList<>();

    }

    public void addSubtasksInEpic(int idSubtask) {
        subtasksId.add(idSubtask);

    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtask () {
        subtasksId.clear();
    }
}






