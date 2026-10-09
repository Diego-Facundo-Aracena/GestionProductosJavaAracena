package tienda;

/** Representa un producto y la cantidad solicitada dentro de un pedido. */
public class LineaPedido {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnitario;

    public LineaPedido(Producto producto, int cantidad) {
        if (producto == null) throw new IllegalArgumentException("El producto es obligatorio.");
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio(); // conserva el precio al momento de crear el pedido
    }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return precioUnitario * cantidad; }
    @Override public String toString() {
        return String.format("%s x %d | $%.2f c/u | Subtotal: $%.2f", producto.getNombre(), cantidad, precioUnitario, getSubtotal());
    }
}
