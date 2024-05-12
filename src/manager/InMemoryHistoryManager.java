package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(task, null, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private void removeNode(Node removeNode) {
        if (removeNode != null) {
            final Node next = removeNode.next;
            final Node prev = removeNode.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
            }
        }
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;

        while (node != null) {
            historyList.add(node.data);
            node = node.next;
        }
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(history.get(task.getId()));
            history.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    private static class Node {
        Task data;
        Node next;
        Node prev;

        public Node(Task data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

    }
}
