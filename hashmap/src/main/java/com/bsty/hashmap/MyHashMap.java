package com.bsty.hashmap;

public class MyHashMap<K,V> implements Map{
    //扩充
    float loadFactor = 0.75f;
    //16 1<<4
    static int threshold = 0;
    Node[] table = null;

    public MyHashMap() {
        threshold = 1 << 4;
    }

    /**
     * 位运算
     *
     * @param initialCapacity
     * @param loadFactor
     */
    public MyHashMap(int initialCapacity, float loadFactor) {

    }

    //数组容量的确定
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    @Override
    public Object put(Object key, Object value) {
        synchronized (MyHashMap.class){
            if (table == null){
                table = new Node[threshold];
            }

//            hash(key) & threshold;
        }
        return null;
    }

    private int hash(Object key) {
        return 0;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    class Node<K,V> implements Entry<K,V>{
        private K key;
        private V value;
        //单向链表
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue ;
        }
    }
}