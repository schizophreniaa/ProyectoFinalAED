package estructura;

public class ArrayList<T> {
    private Object[] elements;
    private int size;

    public ArrayList() {
        this.elements = new Object[10];
        this.size = 0;
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity <= 0) {
            this.elements = new Object[10];
        } else {
            this.elements = new Object[initialCapacity];
        }
        this.size = 0;
    }

    public void add(T element) {
        if (size == elements.length) {
            resize();
        }
        elements[size++] = element;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Índice fuera de rango: " + index);
        }

        if (size == elements.length) {
            resize();
        }

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Índice fuera de rango: " + index);
        }
        return (T) elements[index];
    }

    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Índice fuera de rango: " + index);
        }
        elements[index] = element;
    }

    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        elements = new Object[10];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Índice fuera de rango: " + index);
        }

        T removedElement = (T) elements[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // ayuda al GC
        return removedElement;
    }

    private void resize() {
        Object[] newElements = new Object[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }

    public void reverse() {
        throw new UnsupportedOperationException("Unimplemented method 'reverse'");
    }

    public boolean remove(T item) {
    for (int i = 0; i < size; i++) {
        if (elements[i].equals(item)) {
            int numMoved = size - i - 1;
            if (numMoved > 0) {
                System.arraycopy(elements, i + 1, elements, i, numMoved);
            }
            elements[--size] = null; // ayuda al GC
            return true;
        }
    }
    return false;
}

}
