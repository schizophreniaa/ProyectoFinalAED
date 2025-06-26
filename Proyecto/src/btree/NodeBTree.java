package btree;

import estructura.ArrayList;

public class NodeBTree<E extends Comparable<E>> {
    public ArrayList<E> keys;
    public ArrayList<NodeBTree<E>> childs;
    public int count;
    public int idNode;
    public static int idCounter = 0;
    public boolean isLeaf;
    public NodeBTree<E> next;  
    // Constructor
    public NodeBTree(int n, boolean isLeaf) {
        this.keys = new ArrayList<E>(n);
        this.childs = new ArrayList<NodeBTree<E>>(n + 1);
        this.count = 0;
        this.idNode = idCounter++;
        this.isLeaf = isLeaf;
        this.next = null;

        for (int i = 0; i < n; i++) {
            this.keys.add(null);
            this.childs.add(null);
        }
        this.childs.add(null); // espacio extra para n+1 hijos
    }

    // Verifica si el nodo está lleno
    public boolean nodeFull(int maxKeys) {
        return count == maxKeys;
    }

    // Verifica si el nodo está vacío
    public boolean nodeEmpty() {
        return count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        int i = 0;
        while (i < count && key.compareTo(keys.get(i)) > 0) {
            i++;
        }

        if (i < count && key.compareTo(keys.get(i)) == 0) {
            pos[0] = i;
            return true;
        } else {
            pos[0] = i;
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node ID: ").append(idNode).append(" | Keys: ");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i)).append(" ");
        }
        return sb.toString().trim();
    }

    // Getters y Setters
    public int getIdNode() {
        return idNode;
    }

    public void setIdNode(int idNode) {
        this.idNode = idNode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<E> getKeys() {
        return keys;
    }

    public ArrayList<NodeBTree<E>> getChilds() {
        return childs;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        this.isLeaf = leaf;
    }

    public NodeBTree<E> getNext() {
        return next;
    }

    public void setNext(NodeBTree<E> next) {
        this.next = next;
    }
}
