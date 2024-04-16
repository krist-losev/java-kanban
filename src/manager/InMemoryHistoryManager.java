package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    public Map<Integer, Node> taskHistory = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (taskHistory.containsKey(task.getIdNumber())) {
            remove(task.getIdNumber());
        }
        linkLast(task);
        taskHistory.put(task.getIdNumber(), tail);

    }

    @Override
    public void remove(int id) {
        removeNode(taskHistory.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newTail = new Node(oldTail, task, null);
        tail = newTail;
        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
            size++;
        }
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return new ArrayList<>(history);
    }

    private void removeNode(Node delNode) {
        if (delNode == head && delNode != tail) {
            head = delNode.next;
        } else if (delNode == tail && delNode != head) {
            tail = tail.prev;
            tail.next = null;
        } else if (delNode != head && delNode != tail) {
            delNode.prev.next = delNode.next;
            delNode.next.prev = delNode.prev;
        } else {
            head = null;
            tail = null;
        }
        taskHistory.remove(delNode.data.getIdNumber());
    }
}
