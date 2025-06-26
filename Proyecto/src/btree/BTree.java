package btree;

public class BTree<E extends Comparable<E>> {
    public NodeBTree<E> root;                     // Raíz del árbol
    public int orden;                          // Orden del árbol
    public boolean up;                         // Indica si se debe promover una clave
    public NodeBTree<E> nDes;                     // Nodo a ser promovido

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void insert(E cl) {
        up = false;
        E mediana;
        NodeBTree<E> pnew;
        if (root == null) {                      // Si el árbol está vacío
            root = new NodeBTree<>(orden, true);    // Crear un nuevo nodo hoja
            root.keys.set(0, cl);                // Insertar la clave
            root.count = 1;                      // Incrementar el contador
        } else {
            mediana = push(this.root, cl);       // Insertar en el árbol
            if (up) {                            // Si se debe promover
                pnew = new NodeBTree<>(orden, false); // Crear un nuevo nodo interno
                pnew.count = 1;
                pnew.keys.set(0, mediana);
                pnew.childs.set(0, this.root);
                pnew.childs.set(1, nDes);
                this.root = pnew;                // Actualizar la raíz
            }
        }
    }

    private E push(NodeBTree<E> current, E cl) {
        int[] pos = new int[1];
        E mediana;

        if (current.isLeaf) {                     // Si es un nodo hoja
            boolean found = current.searchNode(cl, pos);
            if (found) {
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }
            // Insertar en el nodo hoja
            current.keys.set(current.count, cl);
            current.count++;
            if (current.count == orden) {          // Si el nodo hoja está lleno
                mediana = splitLeaf(current);      // Dividir el nodo hoja
            } else {
                up = false;                        // No se necesita promover
                return null;
            }
        } else {
        	
            mediana = push(current.childs.get(pos[0]), cl); // Recursión en el hijo adecuado

            if (up) {
                if (current.nodeFull(orden - 1)) { // Si el nodo interno está lleno
                    mediana = dividedNode(current, mediana, pos[0]);
                } else {
                    putNode(current, mediana, nDes, pos[0]);
                    up = false;
                }
            }
        }
        return mediana;
    }

    private E splitLeaf(NodeBTree<E> leaf) {    
        NodeBTree<E> newLeaf = new NodeBTree<>(orden, true);
        int mid = orden / 2;

        // Mover la mitad de las claves al nuevo nodo
        for (int i = mid; i < leaf.count; i++) {
            newLeaf.keys.set(newLeaf.count++, leaf.keys.get(i));
            leaf.keys.set(i, null);  // Limpiar claves movidas
        }
        leaf.count = mid;                         // Actualizar el conteo del nodo hoja

        // Enlazar los nodos hoja
        newLeaf.next = leaf.next;                 // Conectar el nuevo nodo hoja
        leaf.next = newLeaf;                      // Actualizar el puntero del nodo hoja

        return newLeaf.keys.get(0);               // Retornar la clave promotora correcta (primer clave del nuevo nodo hoja)
    }

    private void putNode(NodeBTree<E> current, E cl, NodeBTree<E> rd, int k) {
        for (int i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }

    private E dividedNode(NodeBTree<E> current, E cl, int k) {
    	NodeBTree<E> rd = nDes;
        int i, posMdna;
        int mid = (orden - 1) / 2;
        posMdna = (k <= mid) ? mid : mid + 1;

        nDes = new NodeBTree<>(orden, current.isLeaf); // *** B+ Tree MOD *** Mantener el tipo de nodo

        for (i = posMdna; i < orden - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }

        nDes.count = (orden - 1) - posMdna;
        current.count = posMdna;

        if (k <= mid) {
            putNode(current, cl, rd, k);
        } else {
            putNode(nDes, cl, rd, k - posMdna);
        }

        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));  // Conecta hijo mediana a lado derecho
        current.count--;

        return median;
    }

    public void delete(E cl) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }

        remove(root, cl);

        // Si la raíz queda sin claves
        if (root.count == 0) {
            if (root.childs.get(0) != null)
                root = root.childs.get(0);
            else
                root = null;
        }
    }

    private void remove(NodeBTree<E> node, E cl) {
        int[] pos = new int[1];
        node.searchNode(cl, pos);
        int idx = pos[0];

        if (idx < node.count && node.keys.get(idx).compareTo(cl) == 0) {
            if (isLeaf(node)) {
                removeFromLeaf(node, idx);
            } else {
                removeFromNonLeaf(node, idx);
            }
        } else {
            if (isLeaf(node)) {
                System.out.println("La clave " + cl + " no está en el árbol.");
                return;
            }

            boolean flag = (idx == node.count);

            if (node.childs.get(idx).count < (orden - 1) / 2 + 1)
                fill(node, idx);

            if (flag && idx > node.count)
                remove(node.childs.get(idx - 1), cl);
            else
                remove(node.childs.get(idx), cl);
        }
    }

    private boolean isLeaf(NodeBTree<E> node) {
        return node.childs.get(0) == null; // Verifica si el nodo es hoja
    }

    private void removeFromLeaf(NodeBTree<E> node, int idx) {
        for (int i = idx + 1; i < node.count; ++i) {
            node.keys.set(i - 1, node.keys.get(i));
        }
        node.keys.set(node.count - 1, null);
        node.count--;
    }

    private void removeFromNonLeaf(NodeBTree<E> node, int idx) {
        E k = node.keys.get(idx);

        if (node.childs.get(idx).count >= (orden - 1) / 2 + 1) {
            E pred = getPredecessor(node, idx);
            node.keys.set(idx, pred);
            remove(node.childs.get(idx), pred);
        } else if (node.childs.get(idx + 1).count >= (orden - 1) / 2 + 1) {
            E succ = getSuccessor(node, idx);
            node.keys.set(idx, succ);
            remove(node.childs.get(idx + 1), succ);
        } else {
            merge(node, idx);
            remove(node.childs.get(idx), k);
        }
    }

    private E getPredecessor(NodeBTree<E> node, int idx) {
    	NodeBTree<E> current = node.childs.get(idx);
        while (!isLeaf(current))
            current = current.childs.get(current.count);
        return current.keys.get(current.count - 1);
    }

    private E getSuccessor(NodeBTree<E> node, int idx) {
    	NodeBTree<E> current = node.childs.get(idx + 1);
        while (!isLeaf(current))
            current = current.childs.get(0);
        return current.keys.get(0);
    }

    private void fill(NodeBTree<E> node, int idx) {
        if (idx != 0 && node.childs.get(idx - 1).count >= (orden - 1) / 2 + 1)
            borrowFromPrev(node, idx);
        else if (idx != node.count && node.childs.get(idx + 1).count >= (orden - 1) / 2 + 1)
            borrowFromNext(node, idx);
        else {
            if (idx != node.count)
                merge(node, idx);
            else
                merge(node, idx - 1);
        }
    }

    private void borrowFromPrev(NodeBTree<E> node, int idx) {
    	NodeBTree<E> child = node.childs.get(idx);
    	NodeBTree<E> brother = node.childs.get(idx - 1);

        for (int i = child.count - 1; i >= 0; --i)
            child.keys.set(i + 1, child.keys.get(i));

        if (!isLeaf(child)) {
            for (int i = child.count; i >= 0; --i)
                child.childs.set(i + 1, child.childs.get(i));
        }

        child.keys.set(0, node.keys.get(idx - 1));
        if (!isLeaf(child))
            child.childs.set(0, brother.childs.get(brother.count));

        node.keys.set(idx - 1, brother.keys.get(brother.count - 1));

        child.count += 1;
        brother.count -= 1;
    }

    private void borrowFromNext(NodeBTree<E> node, int idx) {
    	NodeBTree<E> child = node.childs.get(idx);
    	NodeBTree<E> brother = node.childs.get(idx + 1);

        child.keys.set(child.count, node.keys.get(idx));
        if (!isLeaf(child))
            child.childs.set(child.count + 1, brother.childs.get(0));

        node.keys.set(idx, brother.keys.get(0));

        for (int i = 1; i < brother.count; ++i)
        	brother.keys.set(i - 1, brother.keys.get(i));

        if (!isLeaf(brother)) {
            for (int i = 1; i <= brother.count; ++i)
            	brother.childs.set(i - 1, brother.childs.get(i));
        }

        child.count += 1;
        brother.count -= 1;
    }

    private void merge(NodeBTree<E> node, int idx) {
    	NodeBTree<E> child = node.childs.get(idx);
    	NodeBTree<E> brother = node.childs.get(idx + 1);

        child.keys.set((orden - 1) / 2, node.keys.get(idx));

        for (int i = 0; i < brother.count; ++i)
            child.keys.set(i + (orden - 1) / 2 + 1, brother.keys.get(i));

        if (!isLeaf(child)) {
            for (int i = 0; i <= brother.count; ++i)
                child.childs.set(i + (orden - 1) / 2 + 1, brother.childs.get(i));
        }

        for (int i = idx + 1; i < node.count; ++i)				//Elimina llave del nodo Padre
            node.keys.set(i - 1, node.keys.get(i));
        for (int i = idx + 2; i <= node.count; ++i)				//Elimina puntero al hijo Brother
            node.childs.set(i - 1, node.childs.get(i));

        child.count += brother.count + 1;
        node.count--;
    }
    
    public void remove(E key) {
        if (!isEmpty()) {
            remove(root, key);
            if (root.getCount() == 0 && !root.getChilds().isEmpty()) {
                root = root.getChilds().get(0); // Ajustar raíz si se vació
            }
        }
    }

    // Método público para buscar una clave
    public boolean search(E key) {
        int[] pos = new int[1];
        return root != null && root.searchNode(key, pos);
    }

    // Busca un producto por su ID (solo si E es Producto)
    public Producto searchById(String id) {
        if (root == null) return null;
        // Solo permite si el árbol es de Producto
        if (root.keys.get(0) instanceof Producto) {
            @SuppressWarnings("unchecked")
            NodeBTree<Producto> node = (NodeBTree<Producto>) (NodeBTree<?>) root;
            return searchByIdRec(node, id);
        }
        return null;
    }

    private Producto searchByIdRec(NodeBTree<Producto> node, String id) {
        if (node == null) return null;
        for (int i = 0; i < node.count; i++) {
            Producto p = node.keys.get(i);
            if (p != null && p.getId().equals(id)) return p;
            if (!node.isLeaf) {
                Producto found = searchByIdRec(node.childs.get(i), id);
                if (found != null) return found;
            }
        }
        if (!node.isLeaf) return searchByIdRec(node.childs.get(node.count), id);
        return null;
    }
}
