package estructura;

public class HashSet<T> {
    private HashMap<T, Boolean> map;

    public HashSet() {
        map = new HashMap<>();
    }

    public void add(T element) {
        map.put(element, true);
    }

    public boolean contains(T element) {
        return map.containsKey(element);
    }

    public void clear() {
        map.clear();
    }
}
