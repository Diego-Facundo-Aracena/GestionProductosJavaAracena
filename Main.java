package tienda;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Menú de consola y coordinación de las clases del sistema. */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<Producto> productos = new ArrayList<>();
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static int siguienteIdProducto = 1;
    private static int siguienteIdPedido = 1;

    public static void main(String[] args) {
        boolean salir = false;
        System.out.println("=== Sistema de gestión de productos y pedidos ===");
        while (!salir) {
            mostrarMenu();
            String opcion = leerTexto("Elegí una opción: ").toLowerCase();
            try {
                switch (opcion) {
                    case "1", "a" -> agregarProducto();
                    case "2", "b" -> listarProductos();
                    case "3", "c" -> buscarProducto();
                    case "4", "d" -> actualizarProducto();
                    case "5", "e" -> eliminarProducto();
                    case "6", "f" -> crearPedido();
                    case "7", "g" -> listarPedidos();
                    case "8", "s" -> { salir = true; System.out.println("Programa finalizado."); }
                    default -> System.out.println("Opción inválida. Intentá nuevamente.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("No se pudo completar la operación: " + ex.getMessage());
            }
            System.out.println();
        }
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar producto por ID o nombre");
        System.out.println("4. Actualizar producto");
        System.out.println("5. Eliminar producto");
        System.out.println("6. Crear pedido");
        System.out.println("7. Listar pedidos");
        System.out.println("8. Salir");
    }

    private static void agregarProducto() {
        System.out.println("Tipo de producto: 1) General  2) Perecedero");
        int tipo = leerEntero("Elegí el tipo: ");
        if (tipo != 1 && tipo != 2) throw new IllegalArgumentException("Tipo de producto inválido.");
        String nombre = leerTexto("Nombre: ");
        double precio = leerDouble("Precio: $");
        int stock = leerEntero("Stock inicial: ");
        Producto producto;
        if (tipo == 2) {
            LocalDate fecha;
            try { fecha = LocalDate.parse(leerTexto("Fecha de vencimiento (AAAA-MM-DD): ")); }
            catch (DateTimeParseException ex) { throw new IllegalArgumentException("Fecha inválida. Usá AAAA-MM-DD."); }
            producto = new ProductoPerecedero(siguienteIdProducto++, nombre, precio, stock, fecha);
        } else {
            producto = new Producto(siguienteIdProducto++, nombre, precio, stock);
        }
        productos.add(producto);
        System.out.println("Producto agregado: " + producto.detalle());
    }

    private static void listarProductos() {
        if (productos.isEmpty()) { System.out.println("No hay productos registrados."); return; }
        System.out.println("--- PRODUCTOS ---");
        for (Producto p : productos) System.out.println(p.detalle()); // polimorfismo
    }

    private static void buscarProducto() {
        if (productos.isEmpty()) { System.out.println("No hay productos registrados."); return; }
        String criterio = leerTexto("Ingresá un ID o parte del nombre: ");
        boolean encontrado = false;
        for (Producto p : productos) {
            if (String.valueOf(p.getId()).equals(criterio) || p.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                System.out.println(p.detalle()); encontrado = true;
            }
        }
        if (!encontrado) System.out.println("No se encontraron productos.");
    }

    private static void actualizarProducto() {
        Producto p = buscarPorId(leerEntero("ID del producto a actualizar: "));
        if (p == null) { System.out.println("No existe un producto con ese ID."); return; }
        System.out.println("Dejá vacío un campo para conservar el valor actual.");
        String nombre = leerTexto("Nombre [" + p.getNombre() + "]: ");
        String precio = leerTexto("Precio [" + p.getPrecio() + "]: $");
        String stock = leerTexto("Stock [" + p.getStock() + "]: ");
        if (!nombre.isBlank()) p.setNombre(nombre);
        if (!precio.isBlank()) p.setPrecio(parseDouble(precio, "precio"));
        if (!stock.isBlank()) p.setStock(parseInt(stock, "stock"));
        if (p instanceof ProductoPerecedero pp) {
            String fecha = leerTexto("Vencimiento [" + pp.getFechaVencimiento() + "] (AAAA-MM-DD): ");
            if (!fecha.isBlank()) {
                try { pp.setFechaVencimiento(LocalDate.parse(fecha)); }
                catch (DateTimeParseException ex) { throw new IllegalArgumentException("Fecha inválida. Usá AAAA-MM-DD."); }
            }
        }
        System.out.println("Producto actualizado: " + p.detalle());
    }

    private static void eliminarProducto() {
        int id = leerEntero("ID del producto a eliminar: ");
        Producto p = buscarPorId(id);
        if (p == null) { System.out.println("No existe un producto con ese ID."); return; }
        productos.remove(p);
        System.out.println("Producto eliminado: " + p.getNombre());
    }

    private static void crearPedido() {
        if (productos.isEmpty()) { System.out.println("Primero tenés que cargar productos."); return; }
        Pedido pedido = new Pedido(siguienteIdPedido);
        while (true) {
            listarProductos();
            int id = leerEntero("ID del producto a agregar (0 para terminar): ");
            if (id == 0) break;
            Producto p = buscarPorId(id);
            if (p == null) { System.out.println("No existe un producto con ese ID."); continue; }
            int cantidad = leerEntero("Cantidad: ");
            if (!p.hayStock(cantidad)) {
                System.out.println("Stock insuficiente o cantidad inválida. Disponible: " + p.getStock());
                continue;
            }
            // Descontar solo después de validar; el pedido conserva el precio de este momento.
            p.descontarStock(cantidad);
            pedido.agregarLinea(new LineaPedido(p, cantidad));
            System.out.println("Producto agregado al pedido.");
        }
        if (pedido.estaVacio()) { System.out.println("Pedido cancelado: no se agregaron productos."); return; }
        pedidos.add(pedido);
        siguienteIdPedido++;
        System.out.println("Pedido creado correctamente.\n" + pedido);
    }

    private static void listarPedidos() {
        if (pedidos.isEmpty()) { System.out.println("Todavía no hay pedidos."); return; }
        for (Pedido p : pedidos) System.out.println("\n" + p + "\n--------------------");
    }

    private static Producto buscarPorId(int id) {
        for (Producto p : productos) if (p.getId() == id) return p;
        return null;
    }
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }
    private static int leerEntero(String mensaje) { return parseInt(leerTexto(mensaje), "número entero"); }
    private static int parseInt(String valor, String campo) {
        try { return Integer.parseInt(valor); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingresá un valor válido para " + campo + "."); }
    }
    private static double leerDouble(String mensaje) { return parseDouble(leerTexto(mensaje), "precio"); }
    private static double parseDouble(String valor, String campo) {
        try { return Double.parseDouble(valor.replace(',', '.')); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingresá un valor válido para " + campo + "."); }
    }
}
