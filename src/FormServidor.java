import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.*;


public class FormServidor extends JFrame {
    private JTextArea textArea;
    private ServidorChatTCP servidor;

    public FormServidor() {
        super("SERVIDOR");
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
        // panel.setLayout(null);
        JLabel userLabel = new JLabel("Puerto");
        // userLabel.setBounds(10, 10, 40, 25);
        panel.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setText("49171");
        panel.add(userText);
        JButton iniciaButton = new JButton("Inicia servidor");
        // loginButton.setBounds(10, 80, 80, 25);
        panel.add(iniciaButton);
        // BOTÓN DE PARADA
        JButton paraButton = new JButton("Detener servidor");
        paraButton.setEnabled(false);
        panel.add(paraButton);
        // CREA EL SERVIDOR DE SOCKET QUE ESPERA CONEXIONES
        iniciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciaButton.setEnabled(false);
                paraButton.setEnabled(true);
                String texto = userText.getText();
                Conecta(texto);
            }
        });
        // DESCONECTA EL SERVIDOR DE SOCKETS
        paraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paraButton.setEnabled(false);
                iniciaButton.setEnabled(true);
                Desconecta();
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
        panel.add(areaScrollPane);
        // panel.repaint();
        // this.pack();
    }

    void Conecta(String campo) {
        try {
            int puerto = Integer.parseInt(campo);
            this.textArea.append(getHoraActual() + " Esperando conexión\n");
            this.servidor = new ServidorChatTCP(puerto, this.textArea);
            // ARRANCA EL SERVIDOR TCP DESDE UN NUEVO HILO
            this.servidor.start();
        } catch (Exception excepcion) {
            excepcion.printStackTrace();
        }
    }

    void Desconecta() {
        try {
            this.servidor.cierra();
            this.textArea.append(getHoraActual() + " Conexión cerrada\n");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FormServidor form = new FormServidor();
    }
}
