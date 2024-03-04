package Manager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDegaultHistoryManager());
    }

    public static HistoryManager getDegaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
