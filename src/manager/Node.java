package manager;

import tasks.Task;

import java.util.Objects;

public class Node {

    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node tail) {
        this.prev = prev;
        this.data = data;
        this.next = tail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return Objects.equals(data, node.data) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next, prev);
    }

}




