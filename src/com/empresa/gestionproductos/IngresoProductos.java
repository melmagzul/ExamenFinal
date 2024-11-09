package com.empresa.gestionproductos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class IngresoProductos {
    public static void main(String[] args) {
        ConexionBD conexionBD = new ConexionBD();
        Connection conexion = conexionBD.conectar();

        if (conexion != null) {
            try (Scanner scanner = new Scanner(System.in)) {
                boolean continuar = true;
                
                while (continuar) {
                    System.out.println("Seleccione una opción:");
                    System.out.println("1. Ingresar producto");
                    System.out.println("2. Consultar productos");
                    System.out.println("3. Actualizar producto");
                    System.out.println("4. Eliminar producto");
                    System.out.println("5. Buscar producto por código o nombre");
                    System.out.println("0. Salir");
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea
                    
                    switch (opcion) {
                        case 1:
                            ingresarProducto(conexion, scanner);
                            break;
                        case 2:
                            consultarProductos(conexion);
                            break;
                        case 3:
                            actualizarProducto(conexion, scanner);
                            break;
                        case 4:
                            eliminarProducto(conexion, scanner);
                            break;
                        case 5:
                            buscarProducto(conexion, scanner);
                            break;
                        case 0:
                            continuar = false;
                            break;
                        default:
                            System.out.println("Opción no válida. Intente de nuevo.");
                    }
                }
                
                conexionBD.desconectar();
            }
        }
    }

    public static void ingresarProducto(Connection conexion, Scanner scanner) {
        System.out.println("Ingrese el código del producto: ");
        String codigoProducto = scanner.nextLine();

        System.out.println("Ingrese el nombre del producto: ");
        String nombreProducto = scanner.nextLine();

        System.out.println("Ingrese el precio del producto: ");
        double precioUnitario = scanner.nextDouble();

        System.out.println("Ingrese la cantidad del producto: ");
        int cantidadProducto = scanner.nextInt();

        System.out.println("Ingrese la fecha de vencimiento (YYYY-MM-DD): ");
        String fechaStr = scanner.next();
        Date fechaVencimiento = Date.valueOf(fechaStr);

        Producto producto = new Producto(codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento);
        insertarProducto(conexion, producto);
    }

    public static void insertarProducto(Connection conexion, Producto producto) {
        String sql = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, producto.getCodigoProducto());
            statement.setString(2, producto.getNombreProducto());
            statement.setDouble(3, producto.getPrecioUnitario());
            statement.setInt(4, producto.getCantidadProducto());
            statement.setDate(5, (Date) producto.getFechaVencimiento());

            int filasInsertadas = statement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Producto insertado exitosamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    public static void consultarProductos(Connection conexion) {
        String sql = "SELECT * FROM producto";
        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
             
            while (resultSet.next()) {
                System.out.println("Código: " + resultSet.getString("codigoProducto") +
                                   ", Nombre: " + resultSet.getString("nombreProducto") +
                                   ", Precio: " + resultSet.getDouble("precioUnitario") +
                                   ", Cantidad: " + resultSet.getInt("cantidadProducto") +
                                   ", Fecha de Vencimiento: " + resultSet.getDate("fechaVencimiento"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar los productos: " + e.getMessage());
        }
    }

    public static void actualizarProducto(Connection conexion, Scanner scanner) {
        System.out.println("Ingrese el código del producto a actualizar: ");
        String codigoProducto = scanner.nextLine();

        // Aquí se pueden agregar los campos que se desean actualizar
        System.out.println("Ingrese el nuevo nombre del producto: ");
        String nuevoNombre = scanner.nextLine();

        System.out.println("Ingrese el nuevo precio del producto: ");
        double nuevoPrecio = scanner.nextDouble();

        System.out.println("Ingrese la nueva cantidad del producto: ");
        int nuevaCantidad = scanner.nextInt();

        String sql = "UPDATE producto SET nombreProducto = ?, precioUnitario = ?, cantidadProducto = ? WHERE codigoProducto = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, nuevoNombre);
            statement.setDouble(2, nuevoPrecio);
            statement.setInt(3, nuevaCantidad);
            statement.setString(4, codigoProducto);

            int filasActualizadas = statement.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Producto actualizado exitosamente");
            } else {
                System.out.println("No se encontró el producto con el código proporcionado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    public static void eliminarProducto(Connection conexion, Scanner scanner) {
        System.out.println("Ingrese el código del producto a eliminar: ");
        String codigoProducto = scanner.nextLine();

        String sql = "DELETE FROM producto WHERE codigoProducto = ?";

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, codigoProducto);
            int filasEliminadas = statement.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Producto eliminado exitosamente");
            } else {
                System.out.println("No se encontró el producto con el código proporcionado.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    public static void buscarProducto(Connection conexion, Scanner scanner) {
        System.out.println("Ingrese el código o nombre del producto a buscar: ");
        String criterio = scanner.nextLine();

        String sql = "SELECT * FROM producto WHERE codigoProducto = ? OR nombreProducto = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, criterio);
            statement.setString(2, criterio);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Código: " + resultSet.getString("codigoProducto") +
                                       ", Nombre: " + resultSet.getString("nombreProducto") +
                                       ", Precio: " + resultSet.getDouble("precioUnitario") +
                                       ", Cantidad: " + resultSet.getInt("cantidadProducto") +
                                       ", Fecha de Vencimiento: " + resultSet.getDate("fechaVencimiento"));
                } else {
                    System.out.println("No se encontró el producto.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el producto: " + e.getMessage());
        }
    }

    static void actualizarProducto(Connection conexion, Producto producto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
