package util;

import java.util.Random;

/**
 * Created by Michał Krasoń.
 * @see <a href="ftp://ftp.cs.umd.edu/pub/skipLists/skiplists.pdf">ftp://ftp.cs.umd.edu/pub/skipLists/skiplists.pdf</a>
 */
public class SkipList {
    private final int MAX_LEVEL;
    private final Node NIL;

    private final Random random;

    private int level;
    private Node head;

    public SkipList() {
        this(100);
    }

    public SkipList(int expectedSize) {
        MAX_LEVEL = (int) (Math.log(expectedSize)/Math.log(2));
        NIL = new Node(Integer.MAX_VALUE, null, 0);
        random = new Random();
        level = 1;
        head = new Node(Integer.MIN_VALUE, null, MAX_LEVEL);
        for (int i = 0; i < MAX_LEVEL; i++) {
            head.next[i] = NIL;
        }
    }

    public String search(int key) {
        Node node = head;
        for (int i = level - 1; i >= 0; i++) {
            while ((node.next.length > i) && (node.next[i].key < key)) {
                node = node.next[i];
            }
        }
        node = node.next[0];
        if (node.key == key) {
            return node.value;
        } else {
            return null;
        }
    }

    public void insert(int key, String value) {
        Node[] nodes = new Node[MAX_LEVEL];
        Node node = head;
        for (int i = level - 1; i >= 0; i--) {
            while ((node.next.length > i) && (node.next[i].key < key)) {
                node = node.next[i];
            }
            nodes[i] = node;
        }
        node = node.next[0];
        if (node.key == key) {
            node.value = value;
        } else {
            int lvl = generateLevel();
            if (lvl > level) {
                for (int i = level; i < lvl; i++) {
                    nodes[i] = head;
                }
                level = lvl;
            }
            Node newNode = new Node(key, value, lvl);
            for (int i = 0; i < lvl; i++) {
                newNode.next[i] = nodes[i].next[i];
                nodes[i].next[i] = newNode;
            }
        }
    }

    private int generateLevel() {
        int lvl = 1;
        float p = 0.5f;
        while ((random.nextFloat() < p) && (lvl < MAX_LEVEL - 1)) {
            lvl++;
        }
        return lvl;
    }

    public void delete(int key) {
        Node[] nodes = new Node[MAX_LEVEL];
        Node node = head;
        for (int i = level - 1; i >= 0; i--) {
            while ((node.next.length > i) && (node.next[i].key < key)) {
                node = node.next[i];
            }
            nodes[i] = node;
        }
        node = node.next[0];
        if (node.key == key) {
            for (int i = 0; i < level; i++) {
                if (nodes[i].next[i] != node) {
                    break;
                } else {
                    nodes[i].next[i] = node.next[i];
                }
            }
            System.gc();
            while ((level > 1) && (head.next[level - 1] == NIL)) {
                level--;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("SkipList{");
        Node node = head;
        while (node.next[0] != NIL) {
            node = node.next[0];
            string.append(node);
        }
        string.append("}");
        return string.toString();
    }

    private class Node {
        private int key;
        private String value;
        private Node[] next;

        Node(int key, String value, int level) {
            this.key = key;
            this.value = value;
            next = new Node[level];
            for (int i = 0; i < level; i++) {
                next[i] = NIL;
            }
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
