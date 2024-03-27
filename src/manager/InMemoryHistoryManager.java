package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int size = 0;
    private Map<Integer, Node> taskHistory = new HashMap<>();


    @Override
    public void add(Task task) {
        if (taskHistory.containsKey(task.getIdNumber())) {
            removeNode(taskHistory.remove(task.getIdNumber()));
        }
        taskHistory.put(task.getIdNumber(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        removeNode(taskHistory.remove(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newTail = new Node(oldTail, task, null);
        tail = newTail;
        taskHistory.put(task.getIdNumber(), newTail);
        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
            size++;
        }
        return newTail;
    }

    public List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return history;
    }

    public void removeNode(Node delNode) {
        if (delNode == head && delNode != tail) {
            head = delNode.next;
        } else if (delNode == tail && delNode != head) {
            tail = delNode.prev;
        } else if (delNode != head && delNode != tail) {
            delNode.prev.next = delNode.next;
            delNode.next.prev = delNode.prev;
        } else {
            head = null;
            tail = null;
        }
    }
}
