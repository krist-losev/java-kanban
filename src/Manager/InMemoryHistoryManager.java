package Manager;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        /* в этом месте почему-то не работает реализация со списком
        и продолжают добавляться
        просмотры в список
           if (history.size() > 10) {
           history.removeFirst();
        } */
            history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        if (history.size() > 10) {
            history.removeFirst();
        }
       return new ArrayList<>(history);
    }
}
