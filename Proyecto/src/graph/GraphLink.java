package graph;

import estructura.*;

public class GraphLink<E> {
    public ListLinked<Vertex<E>> listVertex;
    
    public GraphLink() {
        this.listVertex = new ListLinked<>();
    }

    public void insertVertex(E data) {
        if (!searchVertex(data)) {
            Vertex<E> nuevo = new Vertex<>(data);
            listVertex.addLast(nuevo);
        } else {
            System.out.println("Duplicado");
        }
    }

    private boolean searchVertex(E data) {
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (aux.data.getData().equals(data)) {
                return true;
            }
            aux = aux.next;
        }
        return false;
    }

    private Vertex<E> getVertex(E data) {
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (aux.data.getData().equals(data)) {
                return aux.data;
            }
            aux = aux.next;
        }
        return null;
    }

    public void insertEdge(E data1, E data2) {
        insertEdgeWeight(data1, data2, 1);
    }

    // Permite insertar arista ponderada directamente (usado por la interfaz)
    public void insertEdge(E data1, E data2, int weight) {
        insertEdgeWeight(data1, data2, weight);
    }

    public void insertEdgeWeight(E data1, E data2, int weight) {
    	if (weight < 0) {
            throw new IllegalArgumentException("El peso de la arista no puede ser negativo.");
        }
        Vertex<E> v1 = getVertex(data1);
        Vertex<E> v2 = getVertex(data2);

        if (v1 == null || v2 == null) return;

        Edge<E> edge1 = new Edge<>(v2, weight);
        if (!v1.listAdj.contains(edge1)) {
            v1.listAdj.addLast(edge1);
        }
    }

    public boolean searchEdge(E verOri, E verDes) {
        Vertex<E> vOri = getVertex(verOri);
        Vertex<E> vDes = getVertex(verDes);
        if (vOri == null || vDes == null) return false;

        return vOri.listAdj.contains(new Edge<>(vDes));
    }

    public Edge<E> getEdge(E verOri, E verDes) {
        Vertex<E> vOri = getVertex(verOri);
        Vertex<E> vDes = getVertex(verDes);
        if (vOri == null || vDes == null) return null;

        Node<Edge<E>> aux = vOri.listAdj.getHead();
        while (aux != null) {
            if (aux.data.getDestination().equals(vDes)) {
                return aux.data;
            }
            aux = aux.next;
        }
        return null;
    }

    public void removeVertex(E data) {
        Vertex<E> vertToRemove = getVertex(data);
        if (vertToRemove == null) return;

        Node<Vertex<E>> auxV = listVertex.getHead();

        while (auxV != null) {
            if (!auxV.data.equals(vertToRemove)) {
                Node<Edge<E>> currEdge = auxV.data.listAdj.getHead();
                while (currEdge != null) {
                    if (currEdge.data.getDestination().equals(vertToRemove)) {
                        auxV.data.listAdj.remove(currEdge.data);
                        break; 
                    }
                    currEdge = currEdge.next;
                }
            }
            auxV = auxV.next;
        }
        listVertex.remove(vertToRemove);
    }

    public void removeEdge(E verOri, E verDes) {
        Vertex<E> vOri = getVertex(verOri);
        Vertex<E> vDes = getVertex(verDes);
        if (vOri == null || vDes == null) return;

        Node<Edge<E>> auxE = vOri.listAdj.getHead();
        while (auxE != null) {
            if (auxE.data.getDestination().equals(vDes)) {
                vOri.listAdj.remove(auxE.data);
                break;
            }
            auxE = auxE.next;
        }
    }

    public ArrayList<E> dfs(E startData) {
    Vertex<E> start = getVertex(startData);
    ArrayList<E> recorrido = new ArrayList<>();
    if (start == null) return recorrido;

    ListLinked<Vertex<E>> visited = new ListLinked<>();
    dfsVisit(start, visited, recorrido);
    return recorrido;
}

private void dfsVisit(Vertex<E> vertex, ListLinked<Vertex<E>> visited, ArrayList<E> recorrido) {
    visited.addLast(vertex);
    recorrido.add(vertex.getData());  // Guardar en la lista el dato
    System.out.println("Visitando: " + vertex.getData());

    Node<Edge<E>> adj = vertex.listAdj.getHead();
    while (adj != null) {
        Vertex<E> adjVertex = adj.data.getDestination();
        if (!visited.contains(adjVertex)) {
            dfsVisit(adjVertex, visited, recorrido);
        }
        adj = adj.next;
    }
}
    private void dfsVisit(Vertex<E> vertex, ListLinked<Vertex<E>> visited) {
    visited.addLast(vertex);

    Node<Edge<E>> adj = vertex.listAdj.getHead();
    while (adj != null) {
        Vertex<E> adjVertex = adj.data.getDestination();
        if (!visited.contains(adjVertex)) {
            dfsVisit(adjVertex, visited);
        }
        adj = adj.next;
    }
}

    public boolean isConexo() {
        if (listVertex.isEmpty()) return true;

        Vertex<E> inicio = listVertex.getHead().data;
        ListLinked<Vertex<E>> visitados = new ListLinked<>();
        dfsVisit(inicio, visitados);

        int totalVertices = 0;
        for (Node<Vertex<E>> aux = listVertex.getHead(); aux != null; aux = aux.next) {
            totalVertices++;
        }

        return visitados.size() == totalVertices;
    }

    public ArrayList<E> bfsPath(E v, E z) {
        Vertex<E> start = getVertex(v);
        Vertex<E> end = getVertex(z);

        ArrayList<E> path = new ArrayList<>();
        if (start == null || end == null) return path;

        HashSet<Vertex<E>> visited = new HashSet<>();
        HashMap<Vertex<E>, Vertex<E>> prev = new HashMap<>();
        Queue<Vertex<E>> queue = new Queue<>();

        visited.add(start);
        queue.enqueue(start);

        while (!queue.isEmpty()) {
            Vertex<E> current = queue.dequeue();

            if (current.equals(end)) break;

            Node<Edge<E>> aux = current.listAdj.getHead();
            while (aux != null) {
                Vertex<E> neighbor = aux.data.getDestination();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    prev.put(neighbor, current);
                    queue.enqueue(neighbor);
                }
                aux = aux.next;
            }
        }

        if (!visited.contains(end)) return path;

        ArrayList<E> reversePath = new ArrayList<>();
        Vertex<E> step = end;
        while (step != null) {
            reversePath.add(step.getData());
            step = prev.get(step);
        }

        for (int i = reversePath.size() - 1; i >= 0; i--) {
            path.add(reversePath.get(i));
        }

        return path;
    }

    public ArrayList<Vertex<E>> shortPath(E v, E z) {
        Vertex<E> start = getVertex(v);
        Vertex<E> end = getVertex(z);

        if (start == null || end == null) return null;

        PriorityQueue<Vertex<E>> pq = new PriorityQueue<>();
        HashMap<Vertex<E>, Integer> dist = new HashMap<>();
        HashMap<Vertex<E>, Vertex<E>> prev = new HashMap<>();
        HashSet<Vertex<E>> visited = new HashSet<>();

        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            dist.put(aux.data, Integer.MAX_VALUE);
            aux.data.dist = Integer.MAX_VALUE;
            aux = aux.next;
        }

        dist.put(start, 0);
        start.dist = 0;
        pq.enqueue(start);

        while (!pq.isEmpty()) {
            Vertex<E> current = pq.dequeue();

            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(end)) break;

            Node<Edge<E>> adj = current.listAdj.getHead();
            while (adj != null) {
                Vertex<E> neighbor = adj.data.getDestination();
                int weight = adj.data.getWeight();

                if (!visited.contains(neighbor)) {
                    int newDist = dist.get(current) + weight;
                    if (newDist < dist.get(neighbor)) {
                        dist.put(neighbor, newDist);
                        neighbor.dist = newDist;
                        prev.put(neighbor, current);
                        pq.enqueue(neighbor);
                    }
                }
                adj = adj.next;
            }
        }

        ArrayList<Vertex<E>> path = new ArrayList<>();
        Vertex<E> temp = end;

        while (temp != null) {
            path.add(0, temp);
            temp = prev.get(temp);
        }

        if (!path.isEmpty() && !path.get(0).equals(start)) return null;

        return path;
    }

    public boolean esZonaAislada(E dato) {
    Vertex<E> vertice = getVertex(dato);
    if (vertice == null) return false;

    // Verificar si no tiene adyacencias (aristas salientes)
    if (!vertice.listAdj.isEmpty()) return false;

    // Verificar si no tiene aristas entrantes desde otros vértices
    Node<Vertex<E>> aux = listVertex.getHead();
    while (aux != null) {
        Vertex<E> otro = aux.data;
        if (!otro.equals(vertice)) {
            Node<Edge<E>> arista = otro.listAdj.getHead();
            while (arista != null) {
                if (arista.data.getDestination().equals(vertice)) {
                    return false; // Tiene una entrada
                }
                arista = arista.next;
            }
        }
        aux = aux.next;
    }

    return true; // No tiene entradas ni salidas → está aislado
}

    public ArrayList<E> zonasAisladas() {
    ArrayList<E> aislados = new ArrayList<>();

    Node<Vertex<E>> aux = listVertex.getHead();
    while (aux != null) {
        if (esZonaAislada(aux.data.getData())) {
            aislados.add(aux.data.getData());
        }
        aux = aux.next;
    }

    return aislados;
}

    public ArrayList<E> dijkstraPath(E origen, E destino) {
    Vertex<E> start = getVertex(origen);
    Vertex<E> end = getVertex(destino);
    ArrayList<E> path = new ArrayList<>();

    if (start == null || end == null) return path;

    HashMap<Vertex<E>, Integer> dist = new HashMap<>();
    HashMap<Vertex<E>, Vertex<E>> prev = new HashMap<>();
    HashSet<Vertex<E>> visited = new HashSet<>();
    ArrayList<Vertex<E>> vertices = new ArrayList<>();

    // Inicializar distancias
    for (Node<Vertex<E>> aux = listVertex.getHead(); aux != null; aux = aux.next) {
        dist.put(aux.data, Integer.MAX_VALUE);
        vertices.add(aux.data);
    }

    dist.put(start, 0);

    while (!vertices.isEmpty()) {
        // Buscar el nodo con menor distancia no visitado
        Vertex<E> u = null;
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<E> v = vertices.get(i);
            if (dist.get(v) < minDist) {
                minDist = dist.get(v);
                u = v;
            }
        }

        if (u == null || u.equals(end)) break;

        vertices.remove(u);
        visited.add(u);

        Node<Edge<E>> adj = u.listAdj.getHead();
        while (adj != null) {
            Vertex<E> v = adj.data.getDestination();
            int weight = adj.data.getWeight();
            if (!visited.contains(v)) {
                int newDist = dist.get(u) + weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                }
            }
            adj = adj.next;
        }
    }

    if (!prev.containsKey(end) && !start.equals(end)) return path;

    Vertex<E> current = end;
    while (current != null) {
        path.add(0, current.getData());
        current = prev.get(current);
    }

    return path;
}

    @Override
    public String toString() {
        return listVertex.toString();
    }

    // Métodos complementarios para grafo dirigido y ponderado exclusivamente

    public int gradoNodo(E data) {
        Vertex<E> v = getVertex(data);
        if (v == null) return -1;
        return v.listAdj.size(); // En grafo dirigido, es el grado de salida
    }

    public boolean esCamino() {
        if (!isConexo()) return false;

        int countGrado1 = 0;
        int countGrado2 = 0;
        int n = listVertex.size();

        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            int grado = aux.data.listAdj.size();
            if (grado == 1) countGrado1++;
            else if (grado == 2) countGrado2++;
            else return false; // algún nodo no cumple con el patrón

            aux = aux.next;
        }
        return (countGrado1 == 2 && countGrado2 == (n - 2));
    }

    public boolean esCiclo() {
        if (!isConexo()) return false;

        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (aux.data.listAdj.size() != 1) return false;
            aux = aux.next;
        }
        return true;
    }

    public boolean esCompleto() {
        int n = listVertex.size();
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (aux.data.listAdj.size() != n - 1) return false;
            aux = aux.next;
        }
        return true;
    }

    public String representacionFormal() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices:\n");
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            sb.append(aux.data.getData()).append(" ");
            aux = aux.next;
        }

        sb.append("\nAristas:\n");
        aux = listVertex.getHead();
        while (aux != null) {
            Node<Edge<E>> adj = aux.data.listAdj.getHead();
            while (adj != null) {
                sb.append("(").append(aux.data.getData())
                  .append(", ").append(adj.data.getDestination().getData())
                  .append(", ").append(adj.data.getWeight()) // Incluyendo el peso
                  .append(") ");
                adj = adj.next;
            }
            aux = aux.next;
        }
        return sb.toString();
    }

    public String listaAdyacencia() {
        StringBuilder sb = new StringBuilder();
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            sb.append(aux.data.getData()).append(": ");
            Node<Edge<E>> adj = aux.data.listAdj.getHead();
            while (adj != null) {
                sb.append(adj.data.getDestination().getData()).append(" (").append(adj.data.getWeight()).append(") "); // Incluyendo el peso
                adj = adj.next;
            }
            sb.append("\n");
            aux = aux.next;
        }
        return sb.toString();
    }

    public int[][] matrizAdyacencia() {
        int n = listVertex.size();
        int[][] matriz = new int[n][n];

        @SuppressWarnings("unchecked")
        Vertex<E>[] vertices = (Vertex<E>[]) new Vertex[n];
        int idx = 0;
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            vertices[idx++] = aux.data;
            aux = aux.next;
        }

        for (int i = 0; i < n; i++) {
            Node<Edge<E>> adj = vertices[i].listAdj.getHead();
            while (adj != null) {
                for (int j = 0; j < n; j++) {
                    if (adj.data.getDestination().equals(vertices[j])) {
                        matriz[i][j] += adj.data.getWeight();  // Incrementar contador para cada arista encontrada
                        break;
                    }
                }
                adj = adj.next;
            }
        }
        return matriz;
    }

    public String matrizAdyacenciaString() {
        int[][] matriz = matrizAdyacencia();
        StringBuilder sb = new StringBuilder();
        int n = matriz.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(matriz[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public boolean tieneCiclo() {
        ListLinked<Vertex<E>> visitados = new ListLinked<>();
        ListLinked<Vertex<E>> enStack = new ListLinked<>();

        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (!visitados.contains(aux.data)) {
                if (tieneCicloDFS(aux.data, visitados, enStack)) {
                    return true;
                }
            }
            aux = aux.next;
        }
        return false;
    }

    private boolean tieneCicloDFS(Vertex<E> v, ListLinked<Vertex<E>> visitados, ListLinked<Vertex<E>> enStack) {
        visitados.addLast(v);
        enStack.addLast(v);

        Node<Edge<E>> adj = v.listAdj.getHead();
        while (adj != null) {
            Vertex<E> vecino = adj.data.getDestination();
            if (!visitados.contains(vecino)) {
                if (tieneCicloDFS(vecino, visitados, enStack)) {
                    return true;
                }
            } else if (enStack.contains(vecino)) {
                // Si está en la pila significa ciclo
                return true;
            }
            adj = adj.next;
        }

        enStack.remove(v);
        return false;
    }

    // Modifica el peso de una arista entre dos vértices
    public void modifyEdgeWeight(E origen, E destino, int nuevoPeso) {
        Node<Vertex<E>> aux = listVertex.getHead();
        while (aux != null) {
            if (aux.data.getData().equals(origen)) {
                Node<Edge<E>> ady = aux.data.getListAdj().getHead();
                while (ady != null) {
                    if (ady.data.getDestination().getData().equals(destino)) {
                        ady.data.setWeight(nuevoPeso);
                        return;
                    }
                    ady = ady.next;
                }
            }
            aux = aux.next;
        }
    }


}
