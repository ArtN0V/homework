package homework.first;

import java.util.Objects;

public class MyHashMap<K, V> {
    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int trashold;
    private final float loadFactor;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public MyHashMap(int initCapacity, float loadFactor) {
        int cap = 1;
        while (cap < initCapacity) cap <<= 1;
        this.capacity = cap;
        this.loadFactor = loadFactor;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.trashold = (int) (capacity * loadFactor);
    }

    public MyHashMap(int initCapacity){
        this(initCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(){
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    private int spredHash(Object key){
        if (key == null) return 0;
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    public V get(Object key){
        Node<K, V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    private Node<K, V> getNode(Object key) {
        int hash = spredHash(key);
        int index = (capacity - 1) & hash;
        for (Node<K, V> e = table[index]; e != null ; e = e.next) {
            if (e.hash == hash && Objects.equals(key, e.key)) {
                return e;
            }
        }
        return null;
    }

    public V put(K key, V value) {
        int hash = spredHash(key);
        int index = (capacity - 1) & hash;

        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && Objects.equals(key, e.key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > trashold) resize();
        return null;
    }

    public V remove(Object key) {
        int hash = spredHash(key);
        int index = (capacity - 1) & hash;
        Node<K, V> prev = null;

        for (Node<K, V> e = table[index]; e != null;) {
            Node<K, V> next = e.next;
            if (e.hash == hash && Objects.equals(key, e.key)) {
                if (prev == null){
                    table[index] = next;
                }
                else {
                    prev.next = next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = e.next;
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    public int size() {
        return size;
    }

    private void resize() {
        int newCapacity = capacity << 1;
        Node<K, V>[] newTabble = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> e : table) {
            while (e != null) {
                Node<K, V> next = e.next;
                int newIndex = (newCapacity - 1) & e.hash;
                e.next = newTabble[newIndex];
                newTabble[newIndex] = e;
                e = next;
            }
        }

        table = newTabble;
        capacity = newCapacity;
        trashold = (int) (capacity * loadFactor);
    }

    @Override
    public String toString(){

        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;

        for (Node<K, V> e : table) {
            for (Node<K, V> node = e; node != null; node = node.next) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.key).append("=").append(node.value);
                first = false;
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
