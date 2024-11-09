package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuscarProductoGUI extends JFrame {

    private final JTextField txtCriterio;
    private final JTextArea areaResultado;

    public BuscarProductoGUI() {
        setTitle("Buscar Producto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Crear el panel superior con un campo de texto y un botón de búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        JLabel lblCriterio = new JLabel("Ingrese Código o Nombre del Producto:");
        txtCriterio = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(80, 30));
        btnBuscar.setBackground(new Color(60, 130, 220));
        btnBuscar.setForeground(Color.WHITE);

        panelSuperior.add(lblCriterio, BorderLayout.NORTH);
        panelSuperior.add(txtCriterio, BorderLayout.CENTER);
        panelSuperior.add(btnBuscar, BorderLayout.EAST);

        // Crear el área de resultado de la búsqueda
        areaResultado = new JTextArea(8, 20);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font("Arial", Font.PLAIN, 14));
        areaResultado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Estilo del área de resultado en un JScrollPane
        JScrollPane scrollPaneResultado = new JScrollPane(areaResultado);
        scrollPaneResultado.setBorder(BorderFactory.createTitledBorder("Resultado de Búsqueda"));

        // Agregar acción al botón de búsqueda
        btnBuscar.addActionListener(e -> buscarProducto());

        // Añadir los componentes al frame principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPaneResultado, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarProducto() {
        areaResultado.setText("");  // Limpiar el área de resultado
        String criterio = txtCriterio.getText().trim();

        if (criterio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código o nombre del producto para buscar.");
            return;
        }

        ConexionBD conexionBD = new ConexionBD();
        Connection conexion = conexionBD.conectar();

        String sql = "SELECT * FROM producto WHERE codigoProducto = ? OR nombreProducto = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, criterio);
            statement.setString(2, criterio);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                areaResultado.append("Código: " + resultSet.getString("codigoProducto") + "\n");
                areaResultado.append("Nombre: " + resultSet.getString("nombreProducto") + "\n");
                areaResultado.append("Precio: " + resultSet.getDouble("precioUnitario") + "\n");
                areaResultado.append("Cantidad: " + resultSet.getInt("cantidadProducto") + "\n");
                areaResultado.append("Fecha de Vencimiento: " + resultSet.getDate("fechaVencimiento") + "\n");
            } else {
                areaResultado.append("No se encontró el producto.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar el producto: " + e.getMessage());
        } finally {
            conexionBD.desconectar(conexion);
        }
    }
}