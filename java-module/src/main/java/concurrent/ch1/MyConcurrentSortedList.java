package concurrent.ch1;

import java.util.concurrent.locks.ReentrantLock;

public class MyConcurrentSortedList {

    private Node head;
    private Node tail;

    class Node {

        Node prev, next;
        int value;
        ReentrantLock lock = new ReentrantLock();

        public Node(int value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        public Node() {

        }
    }

    public MyConcurrentSortedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public void insert(int value) {
        Node current = head;
        Node next = current.next;
        current.lock.lock();

        try {
            while (true) {
                next.lock.lock();
                try {
                    if (next == head || next.value > value) {
                        Node newNode = new Node(value, current, next);
                        current.next = newNode;
                        newNode.prev = newNode;
                        return;
                    }
                } finally {
                    current.lock.unlock();
                }
                current = next;
                next = current.next;
            }
        }finally {
            next.lock.unlock();
        }
    }

}
