package graph;

public class Edge<E> {
    private Vertex<E> refDest;
    private int weight;

    public Edge(Vertex<E> refDest) {
        this(refDest, -1);
    }

    public Edge(Vertex<E> refDest, int weight) {
        this.refDest = refDest;
        this.weight = weight;
    }

    public boolean equals(Object o) {		//Compara Edge		
        if (o instanceof Edge<?>) {
            @SuppressWarnings("unchecked")
            Edge<E> e = (Edge<E>) o;
            return this.refDest.equals(e.refDest);
        }
        return false;
    }

    public String toString() {
        if (this.weight > -1)
            return refDest.getData() + " [" + this.weight + "], ";
        else
            return refDest.getData() + ", ";
    }

    public Vertex<E> getDestination() {
        return refDest;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int nuevoPeso) {
        this.weight = nuevoPeso;
    }
}
