package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    //DoubleLinkedList<Task> taskHistory = new DoubleLinkedList<>();
    //private Map<Integer, Node> connectingTasksAndNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.removeFirst();
        }
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
       return history;
    }


}
class DoubleLinkedList <Task> {
    public Node<Task> head;
    public Node <Task> tail;
    public int size = 0;

    DoubleLinkedList<Task> taskDoubleLinkedList = new DoubleLinkedList<>();


    public void linkLast (Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newTail = new Node<> (oldTail, task, null);
        tail = newTail;
        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
            size++;
        }
    }

    public List<Task> getTasks(Task task) {
        taskDoubleLinkedList.linkLast(task);
        ArrayList <Task> taskHistory= new ArrayList<>((Collection) taskDoubleLinkedList);
        return taskHistory;
    }

    public void removeNode(Node<Task> delNode) {
        if (delNode == head) {
            head = delNode.next;
        } else if (delNode == tail) {
            tail = delNode.prev;
        } else if (delNode != head && delNode != tail){
            delNode.next.next = delNode.next;
            delNode.prev.prev = delNode.prev;
        }
    }
}