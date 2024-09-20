package examen2lab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MainGUI {
    private PSNUsers USERS;
    private JFrame frame;
    private JTextField txtUser, txtGame, txtName, txtDescription;
    private JComboBox<PSNUsers.Trophy> ComboBox;
    private JTextArea infoUser;

    public MainGUI() throws IOException {
        USERS = new PSNUsers();
        frame = new JFrame("Registro de Usuarios PSN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(mainPanel);

        // Título principal
        JLabel titleLabel = new JLabel("Registro de Usuarios PSN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de gestión de usuarios
        JPanel userPanel = createSectionPanel("Registro de Usuarios");
        userPanel.setLayout(new GridLayout(5, 1, 10, 10));

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userPanel.add(userLabel);

        txtUser = new JTextField(20);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 16));
        userPanel.add(txtUser);

        JButton addButton = createStyledButton("Agregar Usuario", new Color(34, 139, 34));
        userPanel.add(addButton);

        JButton deactivateButton = createStyledButton("Desactivar Usuario", new Color(255, 69, 0));
        userPanel.add(deactivateButton);

        JButton searchButton = createStyledButton("Buscar Usuario", new Color(0, 120, 215));
        userPanel.add(searchButton);

        mainPanel.add(userPanel, BorderLayout.WEST);

        // Panel de gestión de trofeos
        JPanel trophyPanel = createSectionPanel("Registro de Trofeos");
        trophyPanel.setLayout(new GridLayout(8, 1, 10, 10));

        JLabel trophyGameLabel = new JLabel("Juego:");
        trophyGameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(trophyGameLabel);

        txtGame = new JTextField(20);
        txtGame.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(txtGame);

        JLabel trophyNameLabel = new JLabel("Nombre del Trofeo:");
        trophyNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(trophyNameLabel);

        txtName = new JTextField(20);
        txtName.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(txtName);

        JLabel descriptionLabel = new JLabel("Descripción del Trofeo:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(descriptionLabel);

        txtDescription = new JTextField(20);
        txtDescription.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(txtDescription);

        JLabel trophyTypeLabel = new JLabel("Tipo de Trofeo:");
        trophyTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(trophyTypeLabel);

        ComboBox = new JComboBox<>(PSNUsers.Trophy.values());
        ComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        trophyPanel.add(ComboBox);

        JButton addTrophyButton = createStyledButton("Agregar Trofeo", new Color(0, 120, 215));
        trophyPanel.add(addTrophyButton);

        mainPanel.add(trophyPanel, BorderLayout.EAST);

        // Área de información del usuario
        JPanel infoPanel = createSectionPanel("Información del Usuario");
        infoPanel.setLayout(new BorderLayout());

        infoUser = new JTextArea();
        infoUser.setEditable(false);
        infoUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(infoUser);
        scrollPane.setPreferredSize(new Dimension(650, 150));
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // ActionListeners
        addButton.addActionListener(e -> agregarUsuario());
        deactivateButton.addActionListener(e -> desactivarUsuario());
        searchButton.addActionListener(e -> buscarUsuario());
        addTrophyButton.addActionListener(e -> agregarTrofeo());

        frame.setVisible(true);
    }

    private void agregarUsuario() {
        String username = txtUser.getText();
        try {
            if (USERS.buscarUsername(username)) {
                JOptionPane.showMessageDialog(null, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                USERS.addUser(username);
                JOptionPane.showMessageDialog(null, "Usuario creado con éxito!", "Creación", 1);
                limpiarCampos();
                actualizarInfo();
            }
        } catch (IOException ex) {
            mostrarError(ex, "Error al agregar usuario.");
        }
    }

    private void desactivarUsuario() {
        String username = txtUser.getText();
        try {
            if (!USERS.buscarUsername(username)) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                USERS.deactivateUser(username);
                JOptionPane.showMessageDialog(null, "El usuario ha sido desactivado", "Desactivación", 1);
                limpiarCampos();
                actualizarInfo();
            }
        } catch (IOException ex) {
            mostrarError(ex, "Error al desactivar usuario.");
        }
    }

    private void buscarUsuario() {
        String username = txtUser.getText();
        String info = "";
        try {
            if (!USERS.buscarUsername(username)) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                info = USERS.playerInfo(username);
                infoUser.setText(info);
            }
        } catch (IOException ex) {
            mostrarError(ex, "Error al buscar usuario.");
        }
    }

    private void agregarTrofeo() {
        String username = txtUser.getText();
        String trophyGame = txtGame.getText();
        String trophyName = txtName.getText();
        String trophyDescription = txtDescription.getText();
        PSNUsers.Trophy type = (PSNUsers.Trophy) ComboBox.getSelectedItem();
        try {
            if (!USERS.buscarUsername(username)) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                USERS.addTrophieTo(username, trophyGame, trophyName, trophyDescription, type);
                JOptionPane.showMessageDialog(null, "Trofeo agregado correctamente", "Creación", 1);
                limpiarCampos();
                actualizarInfo();
            }
        } catch (IOException ex) {
            mostrarError(ex, "Error al agregar trofeo.");
        }
    }

    private void limpiarCampos() {
        txtUser.setText("");
        txtGame.setText("");
        txtName.setText("");
        txtDescription.setText("");
    }

    private void actualizarInfo() {
        infoUser.setText("");
    }

    private void mostrarError(Exception ex, String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje + "\nDetalles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color.darker(), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), new Color(0, 102, 204)));
        return panel;
    }

    public static void main(String[] args) {
        try {
            new MainGUI();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
