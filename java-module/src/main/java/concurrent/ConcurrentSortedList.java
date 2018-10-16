package concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentSortedList {
    private Node head;
    private Node tail;

    private static class Node {
        int value;
        Node next, prev;
        ReentrantLock lock = new ReentrantLock();

        public Node() {
        }

        public Node(int value, Node prev, Node next) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    public ConcurrentSortedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public void insert(int value) {
        Node current = head;
        current.lock.lock();
        Node next = current.next;
        try {
            while (true) {
                next.lock.lock();
                try {
                    if (next == tail || next.value < value) {
                        Node node = new Node(value, current, next);
                        current.next = node;
                        next.prev = node;
                        return;
                    }
                } finally { current.lock.unlock(); }
                current = next;
                next = current.next;
            }
        } finally { next.lock.unlock(); }
    }

    public boolean isSorted() {
        Node current = head;
        while (current.next.next != tail) {
            current = current.next;
            if (current.value < current.next.value)
                return false;
        }
        return true;
    }

    public int size() {
        Node current = tail;
        int count = 0;
        while (current.prev != head) {
            ReentrantLock lock = current.lock;
            lock.lock();
            try {
                ++count;
                current = current.prev;
            } finally { lock.unlock(); }
        }
        return count;
    }

}
