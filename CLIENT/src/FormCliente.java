import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.*;
import java.awt.Dimension;

public class FormCliente extends JFrame {
    private JTextArea textArea;
    private ClienteChatTCP hiloCliente;

    public FormCliente() {
        super("CLIENTE");
        // Especifica el tamaño de la ventana
        this.setSize(420, 550);
        this.setLocationRelativeTo(null);
        // Activa el evento para cierre de la ventana (botón X)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // El frame necesita un panel que contenga todos los elementos del formulario
        JPanel panel = new JPanel();
        this.add(panel);
        // Añadimos los elementos del formulario en el panel en un método aparte por
        // claridad
        elementosPanel(panel);
        // Esto es para que se muestre el frame (la ventana)
        this.setVisible(true);
    }

    private String getHoraActual() {
        LocalDateTime locaDate = LocalDateTime.now();
        int hora = locaDate.getHour();
        int minuto = locaDate.getMinute();
        int segundo = locaDate.getSecond();
        return "[" + hora + ":" + minuto + ":" + segundo + "]";
    }

    private void elementosPanel(JPanel panel) {
        // Eliminamos del panel el layout por defecto
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
        panel.setLayout(null);
        JLabel labelIP = new JLabel("IP");
        labelIP.setBounds(10, 10, 40, 25);
        panel.add(labelIP);
        JTextField textIP = new JTextField(20);
        textIP.setText("localhost");
        textIP.setBounds(70, 10, 100, 25);
        panel.add(textIP);
        JLabel userLabel = new JLabel("Puerto");
        userLabel.setBounds(10, 45, 40, 25);
        panel.add(userLabel);
        JTextField textPuerto = new JTextField(20);
        textPuerto.setText("49171");
        textPuerto.setBounds(70, 45, 100, 25);
        panel.add(textPuerto);
        JButton conectaButton = new JButton("Conecta con servidor");
        conectaButton.setBounds(210, 45, 180, 25);
        panel.add(conectaButton);
        // CONECTA CON EL SOCKET DEL SERVIDOR
        conectaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectaButton.setEnabled(false);
                String textoIP = textIP.getText();
                String textoPuerto = textPuerto.getText();
                Conecta(textoIP, textoPuerto);
            }
        });
        this.textArea = new JTextArea();
        // textArea.setFont(new Font("Serif", Font.ITALIC, 16));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        JScrollPane areaScrollPane = new JScrollPane(this.textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(400, 250));
        areaScrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Conversación"),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)),
                        areaScrollPane.getBorder()));
        areaScrollPane.setBounds(10, 80, 380, 350);
        panel.add(areaScrollPane);
        JTextField mensaje = new JTextField(20);
        mensaje.setBounds(10, 450, 270, 25);
        panel.add(mensaje);
        JButton enviaTxtButton = new JButton("Envía");
        enviaTxtButton.setBounds(300, 450, 75, 25);
        panel.add(enviaTxtButton);
        // ENVIA EL MENSAJE POR EL SOCKET
        enviaTxtButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txtMensaje = mensaje.getText();
                // Utilizamos el hilo para enviar el texto por el socket
                if (hiloCliente.enviaTxt(txtMensaje)) {
                    textArea.append(getHoraActual() + " -> " + txtMensaje + "\n");
                    mensaje.setText("");
                }
            }
        });
        // panel.repaint();
    }

    private void Conecta(String strIP, String strPuerto) {
        try {
            this.textArea.append(getHoraActual() +
                    " Conectando a " + strIP + ":" + strPuerto + "\n");
            int puerto = Integer.parseInt(strPuerto);
            // ABRIMOS EL SOCKET EN UN HILO APARTE
            this.hiloCliente = new ClienteChatTCP(strIP, puerto);
            this.hiloCliente.start();
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FormCliente form = new FormCliente();
    }
}
