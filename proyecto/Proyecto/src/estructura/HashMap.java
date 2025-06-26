package estructura;

public class HashMap<K, V> {
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node<K, V>[] buckets;
    private int capacity;
    private int size;

    @SuppressWarnings("unchecked")
    public HashMap() {
        capacity = 64; // capacidad inicial
        buckets = (Node<K, V>[]) new Node[capacity];
        size = 0;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> head = buckets[index];

        // Buscar si ya existe la clave y actualizar
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Insertar nuevo nodo al inicio de la lista
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // Aquí podrías agregar redimensionamiento si quieres
    }

    public V get(K key) {
        int index = hash(key);
        Node<K, V> head = buckets[index];
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        Node<K, V> head = buckets[index];
        while (head != null) {
            if (head.key.equals(key)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    public void remove(K key) {
        int index = hash(key);
        Node<K, V> head = buckets[index];
        Node<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key)) {
                if (prev == null) {
                    buckets[index] = head.next;
                } else {
                    prev.next = head.next;
                }
                size--;
                return;
            }
            prev = head;
            head = head.next;
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            buckets[i] = null;
        }
        size = 0;
    }
    
    public ArrayList<V> values() {
        ArrayList<V> result = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Node<K, V> node = buckets[i];
            while (node != null) {
                result.add(node.value);
                node = node.next;
            }
        }
        return result;
    }

    public ArrayList<K> keySet() {
        ArrayList<K> keys = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Node<K, V> current = buckets[i];
            while (current != null) {
                keys.add(current.key);
                current = current.next;
            }
        }
        return keys;
    }
}
