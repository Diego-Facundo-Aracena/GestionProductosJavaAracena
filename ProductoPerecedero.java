package tienda;

import java.time.LocalDate;

/** Ejemplo de herencia: producto que además registra fecha de vencimiento. */
public class ProductoPerecedero extends Producto {
    private LocalDate fechaVencimiento;

    public ProductoPerecedero(int id, String nombre, double precio, int stock, LocalDate fechaVencimiento) {
        super(id, nombre, precio, stock);
        setFechaVencimiento(fechaVencimiento);
    }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento == null) throw new IllegalArgumentException("La fecha de vencimiento es obligatoria.");
        this.fechaVencimiento = fechaVencimiento;
    }
    @Override public String getTipo() { return "Perecedero"; }
    @Override public String detalle() { return super.detalle() + " | Vence: " + fechaVencimiento; }
}
