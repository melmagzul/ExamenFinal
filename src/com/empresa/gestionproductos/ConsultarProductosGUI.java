package com.empresa.gestionproductos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultarProductosGUI extends JFrame {

    private JTable tablaProductos;

    public ConsultarProductosGUI() {
        setTitle("Consultar Productos");
        setSize(500, 400);
        setLayout(new BorderLayout());

        tablaProductos = new JTable();
        cargarProductos();

        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarProductos() {
        ConexionBD conexionBD = new ConexionBD();
        Connection conexion = conexionBD.conectar();
        String sql = "SELECT * FROM producto";

        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Código");
            modelo.addColumn("Nombre");
            modelo.addColumn("Precio");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Fecha Vencimiento");

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("codigoProducto"),
                    rs.getString("nombreProducto"),
                    rs.getDouble("precioUnitario"),
                    rs.getInt("cantidadProducto"),
                    rs.getDate("fechaVencimiento")
                });
            }
            tablaProductos.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar productos: " + e.getMessage());
        }
    }
}
