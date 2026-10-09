package tienda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Pedido (u Orden): contiene líneas y calcula el total. */
public class Pedido {
    private final int id;
    private final List<LineaPedido> lineas = new ArrayList<>();

    public Pedido(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID del pedido debe ser positivo.");
        this.id = id;
    }
    public int getId() { return id; }
    public List<LineaPedido> getLineas() { return Collections.unmodifiableList(lineas); }
    public boolean estaVacio() { return lineas.isEmpty(); }
    public void agregarLinea(LineaPedido linea) { lineas.add(linea); }
    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) total += linea.getSubtotal();
        return total;
    }
    @Override public String toString() {
        StringBuilder sb = new StringBuilder("Pedido #" + id + "\n");
        for (LineaPedido linea : lineas) sb.append("  - ").append(linea).append("\n");
        sb.append(String.format("TOTAL: $%.2f", calcularTotal()));
        return sb.toString();
    }
}
