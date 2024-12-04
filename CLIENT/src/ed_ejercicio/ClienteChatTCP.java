package ed_ejercicio;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClienteChatTCP extends Thread {
    private String IP;
    private int puerto;
    private Socket socket;
    private InputStream flujo_entrada;
    private InputStreamReader stream_reader;
    private BufferedReader buffer_reader;
    private BufferedWriter buffer_writer;
    private JTextArea textArea;

    public ClienteChatTCP(String strIP, int puerto, JTextArea areaTexto) {
        this.IP = strIP;
        this.puerto = puerto;
        this.textArea = areaTexto;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public boolean enviaTxt(String mensaje) {
        if (!this.socket.isConnected()) {
            return false;
        }
        try {
            this.buffer_writer.write(mensaje);
            this.buffer_writer.newLine();
            this.buffer_writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void inicializarRecepcion(){
        //this.textArea = new JTextArea();
        try {
            this.flujo_entrada = this.socket.getInputStream();
            this.stream_reader = new InputStreamReader(flujo_entrada);
            this.buffer_reader = new BufferedReader(stream_reader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    private void recibeMensajes() {
        // Mientras el socket no se cierre, leemos datos
        inicializarRecepcion();
        String mensaje;
        while (true) {
            if (this.socket.isClosed()) {
                return;
            }
            if (!this.socket.isConnected()) {
                return;
            }
            try {
                mensaje = this.buffer_reader.readLine();
                this.textArea.append(mensaje + "\n");
                System.out.println(mensaje);
                this.textArea.repaint();
            } catch (IOException e) {
                return;
            }
        }
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.IP, this.puerto);
            OutputStream flujo_salida = this.socket.getOutputStream();
            OutputStreamWriter stream_writer = new OutputStreamWriter(flujo_salida);
            this.buffer_writer = new BufferedWriter(stream_writer);
            recibeMensajes();            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}