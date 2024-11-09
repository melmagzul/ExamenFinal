package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActualizarProductoGUI extends JFrame {

    private final JTextField txtCodigo;
    private final JTextField txtNombre;
    private final JTextField txtPrecio;
    private final JTextField txtCantidad;
    private final JTextField txtFechaVencimiento;
    private final JButton btnBuscar;
    private final JButton btnActualizar;

    public ActualizarProductoGUI() {
        setTitle("Actualizar Producto");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el código y botón buscar
        JPanel panelCodigo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelCodigo.add(new JLabel("Código del Producto:"));
        txtCodigo = new JTextField(10);
        panelCodigo.add(txtCodigo);
        btnBuscar = new JButton("Buscar");
        panelCodigo.add(btnBuscar);

        // Panel central para los campos de actualización
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));
        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtCantidad = new JTextField(15);
        txtFechaVencimiento = new JTextField(15);
        
        txtNombre.setEnabled(false);
        txtPrecio.setEnabled(false);
        txtCantidad.setEnabled(false);
        txtFechaVencimiento.setEnabled(false);

        panelCampos.add(new JLabel("Nuevo Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Nuevo Precio:"));
        panelCampos.add(txtPrecio);
        panelCampos.add(new JLabel("Nueva Cantidad:"));
        panelCampos.add(txtCantidad);
        panelCampos.add(new JLabel("Fecha de Vencimiento (YYYY-MM-DD):"));
        panelCampos.add(txtFechaVencimiento);

        // Panel inferior para el botón actualizar
        JPanel panelBotonActualizar = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setEnabled(false);
        panelBotonActualizar.add(btnActualizar);

        // Agregar paneles al frame
        add(panelCodigo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotonActualizar, BorderLayout.SOUTH);

        // Acción para buscar y actualizar
        btnBuscar.addActionListener(e -> buscarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarProducto() {
        String codigo = txtCodigo.getText();

        try {
            ConexionBD conexionBD = new ConexionBD();
            Connection conexion = conexionBD.conectar();

            String sql = "SELECT * FROM producto WHERE codigoProducto = ?";
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.setString(1, codigo);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Producto encontrado. Puede proceder a actualizar.");
                    txtNombre.setEnabled(true);
                    txtPrecio.setEnabled(true);
                    txtCantidad.setEnabled(true);
                    txtFechaVencimiento.setEnabled(true);
                    btnActualizar.setEnabled(true);

                    txtNombre.setText(resultSet.getString("nombreProducto"));
                    txtPrecio.setText(String.valueOf(resultSet.getDouble("precioUnitario")));
                    txtCantidad.setText(String.valueOf(resultSet.getInt("cantidadProducto")));
                    txtFechaVencimiento.setText(String.valueOf(resultSet.getDate("fechaVencimiento")));
                } else {
                    JOptionPane.showMessageDialog(this, "Código de producto no encontrado. Intente con un código diferente.");
                    txtCodigo.requestFocus();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar el producto: " + e.getMessage());
            }
            conexionBD.desconectar(conexion);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Error en la conexión: " + e.getMessage());
        }
    }

    private void actualizarProducto() {
        try {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());
            java.sql.Date fechaVencimiento = java.sql.Date.valueOf(txtFechaVencimiento.getText());

            ConexionBD conexionBD = new ConexionBD();
            Connection conexion = conexionBD.conectar();

            String sql = "UPDATE producto SET nombreProducto = ?, precioUnitario = ?, cantidadProducto = ?, fechaVencimiento = ? WHERE codigoProducto = ?";
            try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                statement.setString(1, nombre);
                statement.setDouble(2, precio);
                statement.setInt(3, cantidad);
                statement.setDate(4, fechaVencimiento);
                statement.setString(5, codigo);

                int filasActualizadas = statement.executeUpdate();
                if (filasActualizadas > 0) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente.");
                    dispose();
                    new GestionProductosGUI();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el producto con el código proporcionado.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el producto: " + e.getMessage());
            }
            conexionBD.desconectar(conexion);
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + e.getMessage());
        }
    }
}
