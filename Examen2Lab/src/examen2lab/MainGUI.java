package examen2lab;

import examen2lab.HashTable.Trophy;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainGUI {
    private PSNUsers USERS;
    private JFrame frame;
    private JTextField txtUser,txtGame,txtName;
    private JComboBox<Trophy> ComboBox;
    private JTextArea infoUser;

    public MainGUI() throws IOException {
        USERS=new PSNUsers();
        frame=new JFrame("PSN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel=new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel userLabel=new JLabel("Usuario:");
        userLabel.setBounds(140, 10, 80, 25);
        panel.add(userLabel);

        txtUser=new JTextField(20);
        txtUser.setBounds(200, 10, 160, 25);
        panel.add(txtUser);

        JButton addButton = new JButton("Agregar Usuario");
        addButton.setBounds(10, 40, 130, 25);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username=txtUser.getText();
                try {
                    USERS.addUser(username);
                    JOptionPane.showMessageDialog(null, "Usuario creado con éxito!", "Creación", 1);
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panel.add(addButton);
        
        JButton deactivateButton = new JButton("Desactivar Usuario");
        deactivateButton.setBounds(210, 40, 145, 25);
        deactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username=txtUser.getText();
                try {
                    USERS.deactivateUser(username);
                    JOptionPane.showMessageDialog(null, "El usuario ha sido desactivado", "Desactivación", 1);

                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panel.add(deactivateButton);
        JButton searchButton = new JButton("Buscar Usuario");
        searchButton.setBounds(420, 40, 130, 25);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username=txtUser.getText();
                String info="";
                try {
                    info=USERS.playerInfo(username);
                }catch(IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                infoUser.setText(info);
            }
        });
        
	panel.add(searchButton);

        JLabel trophyGameLabel=new JLabel("Juego:");
        trophyGameLabel.setBounds(150, 70, 80, 25);
        panel.add(trophyGameLabel);

        txtGame=new JTextField(20);
        txtGame.setBounds(200, 70, 160, 25);
        panel.add(txtGame);

        JLabel trophyNameLabel=new JLabel("Nombre del Trofeo:");
        trophyNameLabel.setBounds(80, 100, 110, 25);
        panel.add(trophyNameLabel);

        txtName=new JTextField(20);
        txtName.setBounds(200, 100, 160, 25);
        panel.add(txtName);

        JLabel trophyTypeLabel = new JLabel("Tipo de Trofeo:");
        trophyTypeLabel.setBounds(100, 130, 85, 25);
        panel.add(trophyTypeLabel);

        ComboBox=new JComboBox<>(Trophy.values());
        ComboBox.setBounds(200, 130, 160, 25);
        panel.add(ComboBox);

        JButton addTrophyButton = new JButton("Agregar Trofeo");
        addTrophyButton.setBounds(220, 160, 120, 25);
        addTrophyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username=txtUser.getText();
                String trophyGame=txtGame.getText();
                String trophyName=txtName.getText();
                Trophy type=(Trophy)ComboBox.getSelectedItem();
                try {
                    USERS.addTrophieTo(username,trophyGame,trophyName,type);
                    JOptionPane.showMessageDialog(null, "El trofeo se ha agregado al usuario", "Creación", 1);
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panel.add(addTrophyButton);

        

        infoUser = new JTextArea();
        infoUser.setBounds(10,200 ,565 ,250 );
        infoUser.setEditable(false);
        infoUser.setFont(new Font("Segoe Print", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(infoUser);
        scrollPane.setBounds(10,200 ,565 ,250 );
        panel.add(scrollPane);	

        frame.setVisible(true);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            new MainGUI();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
