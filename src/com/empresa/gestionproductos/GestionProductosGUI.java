package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;

public class GestionProductosGUI extends JFrame {

    public GestionProductosGUI() {
        setTitle("Gestión de Productos");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245)); // Fondo claro

        // Panel para el logo
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(new Color(0, 153, 153)); // Fondo color
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(new ImageIcon("images/koders.jpg")); // Ruta del logo
        panelLogo.add(lblLogo);

        // Panel de botones de productos
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones.setBackground(new Color(245, 245, 245)); // Fondo claro

        JButton btnIngresar = new JButton("Ingresar Producto");
        JButton btnConsultar = new JButton("Consultar Productos");
        JButton btnActualizar = new JButton("Actualizar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnBuscar = new JButton("Buscar Producto");
        JButton btnFacturar = new JButton("Facturar");

        // Estilo de botones
        Color botonColor = new Color(51, 153, 255);
        Color textoBotonColor = Color.WHITE;
        btnIngresar.setBackground(botonColor);
        btnIngresar.setForeground(textoBotonColor);
        btnConsultar.setBackground(new Color(54, 255, 44));
        btnConsultar.setForeground(textoBotonColor);
        btnActualizar.setBackground(new Color(178, 255, 35));
        btnActualizar.setForeground(textoBotonColor);
        btnEliminar.setBackground(new Color(0, 153, 76));
        btnEliminar.setForeground(textoBotonColor);
        btnBuscar.setBackground(new Color(248, 123, 6));
        btnBuscar.setForeground(textoBotonColor);
        btnFacturar.setBackground(new Color(109, 26, 110));
        btnFacturar.setForeground(textoBotonColor);

        // Agregar botones al panel
        panelBotones.add(btnIngresar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnFacturar);

        // Panel inferior con opción "Salir"
        JPanel panelOpciones = new JPanel();
        panelOpciones.setBackground(new Color(245, 245, 245));
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBackground(new Color(204, 0, 0));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> System.exit(0));
        panelOpciones.add(btnSalir);

        // Acción de cada botón
        btnIngresar.addActionListener(e -> new IngresarProductoGUI());
        btnConsultar.addActionListener(e -> new ConsultarProductosGUI());
        btnActualizar.addActionListener(e -> new ActualizarProductoGUI());
        btnEliminar.addActionListener(e -> new EliminarProductoGUI());
        btnBuscar.addActionListener(e -> new BuscarProductoGUI());
        btnFacturar.addActionListener(e -> new FacturarGUI());

        // Agregar paneles al frame
        add(panelLogo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Mostrar LoginGUI primero
        LoginGUI login = new LoginGUI();
        if (login.isAuthenticated()) {
            GestionProductosGUI GestionProductosGUI = new GestionProductosGUI(); // Abrir gestión de productos solo si el login fue exitoso
        } else {
            System.exit(0); // Cerrar si el login falla
        }
    }
}