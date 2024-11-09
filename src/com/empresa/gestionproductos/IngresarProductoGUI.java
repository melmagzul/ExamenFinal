package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import javax.imageio.ImageIO;

public class IngresarProductoGUI extends JFrame {

    private JTextField txtCodigo, txtNombre, txtPrecio, txtCantidad, txtFechaVencimiento;
    private BufferedImage backgroundImage;

    public IngresarProductoGUI() {
        setTitle("Ingresar Producto");
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Cargar imagen de fondo
        try {
            backgroundImage = ImageIO.read(new File("imagenes/marca_agua.jpg")); // Cambia la ruta según tu imagen de fondo
        } catch (IOException e) {
            System.out.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
        }

        // Panel de formulario con imagen de fondo
        JPanel panelFormulario = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Dibuja la imagen como fondo
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos de texto
        txtCodigo = new JTextField(15);
        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(15);
        txtCantidad = new JTextField(15);
        txtFechaVencimiento = new JTextField(15);

        // Etiquetas y campos
        agregarCampo(panelFormulario, gbc, "Código:", txtCodigo, 0);
        agregarCampo(panelFormulario, gbc, "Nombre:", txtNombre, 1);
        agregarCampo(panelFormulario, gbc, "Precio:", txtPrecio, 2);
        agregarCampo(panelFormulario, gbc, "Cantidad:", txtCantidad, 3);
        agregarCampo(panelFormulario, gbc, "Fecha Vencimiento (YYYY-MM-DD):", txtFechaVencimiento, 4);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false); // Fondo transparente
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilizar botones
        btnGuardar.setBackground(new Color(51, 153, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(204, 0, 0));
        btnCancelar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar paneles al frame principal
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción del botón Guardar
        btnGuardar.addActionListener(e -> guardarProducto());

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> {
            dispose();
            new GestionProductosGUI(); // Volver al menú
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para agregar campos con etiqueta en el panel
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JTextField campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setForeground(Color.BLACK); // Color de las etiquetas
        panel.add(lblEtiqueta, gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        campo.setOpaque(false); // Fondo transparente para los campos
        panel.add(campo, gbc);
    }

    private void guardarProducto() {
        try {
            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());
            Date fechaVencimiento = Date.valueOf(txtFechaVencimiento.getText());

            Producto producto = new Producto(codigo, nombre, precio, cantidad, fechaVencimiento);
            ConexionBD conexionBD = new ConexionBD();
            Connection conexion = conexionBD.conectar();

            IngresoProductos.insertarProducto(conexion, producto);

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente.");

            // Preguntar si desea agregar otro producto
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas agregar otro producto?",
                    "Confirmación", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                limpiarCampos(); // Limpiar campos para ingresar otro producto
            } else {
                dispose(); // Cerrar la ventana actual
                new GestionProductosGUI(); // Regresar al menú
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un valor numérico válido para el precio y cantidad.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error en la fecha de vencimiento: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar producto: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtFechaVencimiento.setText("");
    }
}