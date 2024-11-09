package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EliminarProductoGUI extends JFrame {

    private final JTextField txtCodigo;
    private final JTextArea areaInfoProducto;

    public EliminarProductoGUI() {
        setTitle("Eliminar Producto");
        setSize(400, 250);
        setLayout(new BorderLayout(10, 10));

        // Panel superior para el campo de código y botón de buscar
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        JLabel lblCodigo = new JLabel("Código del Producto:");
        txtCodigo = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar");

        panelSuperior.add(lblCodigo, BorderLayout.WEST);
        panelSuperior.add(txtCodigo, BorderLayout.CENTER);
        panelSuperior.add(btnBuscar, BorderLayout.EAST);

        // Área de texto para mostrar información del producto
        areaInfoProducto = new JTextArea(5, 20);
        areaInfoProducto.setEditable(false);
        areaInfoProducto.setLineWrap(true);
        areaInfoProducto.setWrapStyleWord(true);
        areaInfoProducto.setBorder(BorderFactory.createTitledBorder("Información del Producto"));

        // Botón de eliminar
        JButton btnEliminar = new JButton("Eliminar Producto");
        btnEliminar.setEnabled(false);  // Se habilitará solo si se encuentra el producto

        // Acción de buscar
        btnBuscar.addActionListener(e -> buscarProducto(btnEliminar));

        // Acción de eliminar
        btnEliminar.addActionListener(e -> eliminarProducto());

        // Agregar componentes a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(areaInfoProducto), BorderLayout.CENTER);
        add(btnEliminar, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarProducto(JButton btnEliminar) {
        areaInfoProducto.setText("");  // Limpiar el área de información
        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código de producto para buscar.");
            return;
        }

        ConexionBD conexionBD = new ConexionBD();
        Connection conexion = conexionBD.conectar();

        String sql = "SELECT * FROM producto WHERE codigoProducto = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, codigo);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Mostrar la información del producto en el área de texto
                areaInfoProducto.append("Código: " + resultSet.getString("codigoProducto") + "\n");
                areaInfoProducto.append("Nombre: " + resultSet.getString("nombreProducto") + "\n");
                areaInfoProducto.append("Precio: " + resultSet.getDouble("precioUnitario") + "\n");
                areaInfoProducto.append("Cantidad: " + resultSet.getInt("cantidadProducto") + "\n");
                areaInfoProducto.append("Fecha de Vencimiento: " + resultSet.getDate("fechaVencimiento") + "\n");

                btnEliminar.setEnabled(true);  // Habilitar el botón de eliminar
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con el código proporcionado.");
                btnEliminar.setEnabled(false);  // Deshabilitar el botón de eliminar
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el producto: " + e.getMessage());
        } finally {
            conexionBD.desconectar(conexion);
        }
    }

    private void eliminarProducto() {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este producto?", "Confirmación de Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;  // Cancelar si el usuario selecciona NO
        }

        String codigo = txtCodigo.getText().trim();
        ConexionBD conexionBD = new ConexionBD();
        Connection conexion = conexionBD.conectar();

        String sql = "DELETE FROM producto WHERE codigoProducto = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, codigo);

            int filasEliminadas = statement.executeUpdate();
            if (filasEliminadas > 0) {
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente");

                // Preguntar si desea eliminar otro producto o salir
                int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar otro producto?", "Eliminar Otro Producto", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    txtCodigo.setText("");
                    areaInfoProducto.setText("");
                    txtCodigo.requestFocus();
                } else {
                    dispose();  // Cerrar la ventana si elige no eliminar otro producto
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el producto con el código proporcionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el producto: " + e.getMessage());
        } finally {
            conexionBD.desconectar(conexion);
        }
    }
}