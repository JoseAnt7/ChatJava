package ed_ejercicio;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

public class ServidorChatTCP extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream flujo_entrada;
    private InputStreamReader stream_reader;
    private BufferedReader buffer_reader;
    private BufferedWriter buffer_writer;
    private OutputStream flujo_salida;
    private OutputStreamWriter stream_writer;
    private int puerto;
    private JTextArea textArea;

    public ServidorChatTCP(int puerto, JTextArea areaTexto) {
        this.puerto = puerto;
        this.textArea = areaTexto;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.puerto);
            // Aquí se detiene a la espera de que el cliente establezca la conexión
            this.socket = this.serverSocket.accept();
            System.out.println("Conexión establecida con el cliente");
            // cuando llega la petición del cliente se establece el socket
            // Y continua la ejecución que estaba detenida
            estableceStreamsSocket();
            recibeMensajes();
            enviarMensajes();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cierra() throws IOException {
        this.buffer_reader.close();
        this.socket.close();
        this.serverSocket.close();
    }

    public Socket getSocket() {
        return this.socket;
    }

    private void estableceStreamsSocket() {
        try {
            this.flujo_entrada = this.socket.getInputStream();
            this.stream_reader = new InputStreamReader(flujo_entrada);
            this.buffer_reader = new BufferedReader(stream_reader);
            this.flujo_salida = this.socket.getOutputStream();
            this.stream_writer = new OutputStreamWriter(flujo_salida);
            this.buffer_writer = new BufferedWriter(stream_writer);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void enviarMensajes(){
        try {
            this.flujo_salida = this.socket.getOutputStream();
            this.stream_writer = new OutputStreamWriter(flujo_salida);
            this.buffer_writer = new BufferedWriter(stream_writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void recibeMensajes() {
        // Mientras el socket no se cierre, leemos datos
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
            } catch (IOException e) {
                return;
            }
        }
    }


}
