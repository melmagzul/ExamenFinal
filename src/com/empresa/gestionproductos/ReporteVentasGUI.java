package com.empresa.gestionproductos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReporteVentasGUI extends JFrame {

    public ReporteVentasGUI() {
        setTitle("Reporte de Ventas");
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Modelo de tabla para mostrar las ventas
        DefaultTableModel model = new DefaultTableModel(new String[]{"Cliente", "Producto", "Cantidad", "Total"}, 0);
        JTable table = new JTable(model);

        // Cargar datos de la tabla 'venta'
        cargarDatosVentas(model);

        // Agregar la tabla a un JScrollPane para permitir el desplazamiento
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
    }

    private void cargarDatosVentas(DefaultTableModel model) {
        try (Connection conexion = new ConexionBD().conectar()) {
            String sql = "SELECT nombreCliente, codigoProducto, cantidadVenta, total FROM venta";
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cliente = resultSet.getString("nombreCliente");
                String producto = resultSet.getString("codigoProducto");
                int cantidad = resultSet.getInt("cantidadVenta");
                double total = resultSet.getDouble("total");
                model.addRow(new Object[]{cliente, producto, cantidad, total});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el reporte de ventas: " + e.getMessage());
        }
    }
}
