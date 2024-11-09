package com.empresa.gestionproductos;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JDialog {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private boolean authenticated = false;

    public LoginGUI() {
        setTitle("Iniciar Sesión");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior para el logo (con tamaño reducido)
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(new Color(200, 220, 240)); // Color de fondo del panel de logo

        // Logo reducido
        ImageIcon logoIcon = new ImageIcon("images/koders.jpg");
        Image logoImage = logoIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoImage));
        panelLogo.add(lblLogo);
        add(panelLogo, BorderLayout.NORTH);

        // Panel de formulario de inicio de sesión con fondo
        JPanel panelLogin = new JPanel(new GridBagLayout());
        panelLogin.setBackground(new Color(240, 250, 255)); // Fondo del formulario
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField(15);
        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField(15);
        JButton btnIniciarSesion = new JButton("Iniciar Sesión");
        JLabel lblError = new JLabel("Usuario o contraseña incorrectos", SwingConstants.CENTER);
        lblError.setForeground(Color.RED);
        lblError.setVisible(false);

        // Configuración de diseño
        gbc.gridx = 0; gbc.gridy = 0;
        panelLogin.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panelLogin.add(lblContrasena, gbc);
        gbc.gridx = 1;
        panelLogin.add(txtContrasena, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelLogin.add(btnIniciarSesion, gbc);
        gbc.gridy = 3;
        panelLogin.add(lblError, gbc);

        add(panelLogin, BorderLayout.CENTER);

        // Acción del botón de inicio de sesión
        btnIniciarSesion.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String contrasena = new String(txtContrasena.getPassword());

            if (usuario.equals("mmagzul") && contrasena.equals("Abc123**")) {
                authenticated = true;
                dispose();
            } else {
                lblError.setVisible(true);
            }
        });

        getContentPane().setBackground(new Color(200, 220, 240)); // Fondo para toda la ventana
        setModal(true);
        setVisible(true);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}