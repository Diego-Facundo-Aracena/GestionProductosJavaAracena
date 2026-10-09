package tienda;

/** Clase base de todos los productos. */
public class Producto {
    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser positivo.");
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
        this.id = id;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        this.nombre = nombre.trim();
    }
    public void setPrecio(double precio) {
        if (!Double.isFinite(precio) || precio < 0) throw new IllegalArgumentException("El precio debe ser un número válido y no negativo.");
        this.precio = precio;
    }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
        this.stock = stock;
    }
    public boolean hayStock(int cantidad) { return cantidad > 0 && stock >= cantidad; }
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        if (!hayStock(cantidad)) throw new IllegalStateException("Stock insuficiente para '" + nombre + "'. Disponible: " + stock);
        stock -= cantidad;
    }
    public String getTipo() { return "General"; }
    public String detalle() {
        return String.format("ID: %d | %s | Tipo: %s | Precio: $%.2f | Stock: %d", id, nombre, getTipo(), precio, stock);
    }
    @Override public String toString() { return detalle(); }
}
