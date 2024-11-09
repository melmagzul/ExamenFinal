package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class FacturarGUI extends JFrame {

    private final JTextField txtNombreCliente;
    private final JTextField txtCantidad;
    private final JComboBox<String> comboProductos;
    private final JLabel lblPrecio;
    private JLabel lblExistencias, lblTotal;
    private double precioProducto = 0.0;
    private int existenciasProducto = 0;
    private String codigoProducto;
    private JButton btnBuscar;

    public FacturarGUI() {
        setTitle("Facturación de Producto");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245)); // Fondo claro

        // Encabezado
        JLabel lblTitulo = new JLabel("Factura de Venta", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 153, 153));
        lblTitulo.setForeground(Color.WHITE);

        // Panel de entrada de datos
        JPanel panelEntrada = new JPanel();
        panelEntrada.setLayout(new BoxLayout(panelEntrada, BoxLayout.Y_AXIS)); // Vertical
        panelEntrada.setBackground(new Color(245, 245, 245));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombreCliente = new JTextField();
        txtCantidad = new JTextField();
        comboProductos = new JComboBox<>();
        lblPrecio = new JLabel("Precio:");
        lblExistencias = new JLabel("Existencias:");
        lblTotal = new JLabel("Total:");
        lblPrecio.setForeground(new Color(0, 102, 204));
        lblExistencias.setForeground(new Color(0, 102, 204));
        lblTotal.setForeground(new Color(0, 102, 204));

        // Botones
        btnBuscar = new JButton("Agregar Producto");
        JButton btnFacturar = new JButton("Facturar");
        JButton btnCancelar = new JButton("Cancelar");
        btnBuscar.setBackground(new Color(51, 153, 255));
        btnBuscar.setForeground(Color.WHITE);
        btnFacturar.setBackground(new Color(0, 153, 76));
        btnFacturar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(204, 0, 0));
        btnCancelar.setForeground(Color.WHITE);

        // Cargar productos
        cargarProductos();
        comboProductos.setVisible(false); // Combo oculto al inicio

        // Agregar componentes al panel de entrada
        panelEntrada.add(new JLabel("Nombre del Cliente:", JLabel.LEFT));
        panelEntrada.add(txtNombreCliente);
        panelEntrada.add(btnBuscar);
        panelEntrada.add(new JLabel("Producto:", JLabel.RIGHT));
        panelEntrada.add(comboProductos);
        panelEntrada.add(new JLabel("", JLabel.LEFT));
        panelEntrada.add(lblPrecio);
        panelEntrada.add(new JLabel("", JLabel.LEFT));
        panelEntrada.add(lblExistencias);
        panelEntrada.add(new JLabel("Cantidad a Vender:", JLabel.LEFT));
        panelEntrada.add(txtCantidad);
        panelEntrada.add(new JLabel("", JLabel.LEFT));
        panelEntrada.add(lblTotal);

        // Panel de botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnFacturar);
        panelBotones.add(btnCancelar);
        panelBotones.setBackground(new Color(245, 245, 245));

        // Acciones de los botones
        btnBuscar.addActionListener(e -> mostrarComboProductos());
        btnFacturar.addActionListener(e -> facturarProducto());
        btnCancelar.addActionListener(e -> dispose());

        // Actualizar total al ingresar cantidad
        txtCantidad.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarTotal();
            }
        });

        // Agregar secciones al frame
        add(lblTitulo, BorderLayout.NORTH);
        add(panelEntrada, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarProductos() {
        try (Connection conexion = new ConexionBD().conectar()) {
            String sql = "SELECT codigoProducto, nombreProducto FROM producto";
            PreparedStatement statement = conexion.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String codigo = resultSet.getString("codigoProducto");
                String nombre = resultSet.getString("nombreProducto");
                comboProductos.addItem(codigo + " - " + nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private void mostrarComboProductos() {
        comboProductos.setVisible(true);
        comboProductos.addActionListener(e -> buscarProducto());
    }

    private void buscarProducto() {
        try {
            String selectedItem = (String) comboProductos.getSelectedItem();
            if (selectedItem != null) {
                String codigo = selectedItem.split(" - ")[0];
                try (Connection conexion = new ConexionBD().conectar()) {
                    String sql = "SELECT * FROM producto WHERE codigoProducto = ?";
                    PreparedStatement statement = conexion.prepareStatement(sql);
                    statement.setString(1, codigo);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        codigoProducto = resultSet.getString("codigoProducto");
                        precioProducto = resultSet.getDouble("precioUnitario");
                        existenciasProducto = resultSet.getInt("cantidadProducto");

                        lblPrecio.setText("Precio: " + precioProducto);
                        lblExistencias.setText("Existencias: " + existenciasProducto);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar producto: " + e.getMessage());
        }
    }

    private void actualizarTotal() {
        try {
            int cantidadVenta = Integer.parseInt(txtCantidad.getText());
            double totalVenta = cantidadVenta * precioProducto;
            lblTotal.setText("Total: " + totalVenta);
        } catch (NumberFormatException e) {
            lblTotal.setText("Total: ");
        }
    }

    private void facturarProducto() {
        try {
            String nombreCliente = txtNombreCliente.getText();
            int cantidadVenta = Integer.parseInt(txtCantidad.getText());

            if (cantidadVenta > existenciasProducto) {
                JOptionPane.showMessageDialog(this, "No hay suficiente stock para realizar la venta.");
                return;
            }

            double totalVenta = cantidadVenta * precioProducto;
            lblTotal.setText("Total: " + totalVenta);

            int confirmacion;
            confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea confirmar la venta? ", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                try (Connection conexion = new ConexionBD().conectar()) {
                    String sql = "INSERT INTO venta (nombreCliente, codigoProducto, cantidadVenta, total) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = conexion.prepareStatement(sql);

                    statement.setString(1, nombreCliente);
                    statement.setString(2, codigoProducto);
                    statement.setInt(3, cantidadVenta);
                    statement.setDouble(4, totalVenta);

                    int filasInsertadas = statement.executeUpdate();
                    if (filasInsertadas > 0) {
                        actualizarStock(cantidadVenta);
                        JOptionPane.showMessageDialog(this, "Venta realizada y stock actualizado con éxito.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + e.getMessage());
        }
    }

    private void actualizarStock(int cantidadVendida) {
        try (Connection conexion = new ConexionBD().conectar()) {
            String sql = "UPDATE producto SET cantidadProducto = cantidadProducto - ? WHERE codigoProducto = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, cantidadVendida);
            statement.setString(2, codigoProducto);
            statement.executeUpdate();

            limpiarCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el stock: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombreCliente.setText("");
        txtCantidad.setText("");
        lblPrecio.setText("Precio:");
        lblExistencias.setText("Existencias:");
        lblTotal.setText("Total:");
        comboProductos.setVisible(false); // Ocultar el combo nuevamente
    }
}