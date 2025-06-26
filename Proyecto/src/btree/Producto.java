package btree;

public class Producto implements Comparable<Producto> {
    private String id;
    private String nombre;
    private double precio;
    private int cantidad;
    private int demanda;

    public Producto(String id, String nombre, double precio, int cantidad, int demanda) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.demanda = demanda;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public int getDemanda() { return demanda; }
    public void setDemanda(int demanda) { this.demanda = demanda; }

    @Override
    public int compareTo(Producto otro) {
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString() {
        return "Producto[id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + ", demanda=" + demanda + ", =" + "]";
    }
}
