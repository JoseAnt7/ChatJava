import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClienteChatTCP extends Thread {
    private String IP;
    private int puerto;
    private Socket socket;
    private BufferedReader buffer_reader;
    private BufferedWriter buffer_writer;

    public ClienteChatTCP(String strIP, int puerto) {
        this.IP = strIP;
        this.puerto = puerto;
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

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.IP, this.puerto);
            OutputStream flujo_salida = this.socket.getOutputStream();
            OutputStreamWriter stream_writer = new OutputStreamWriter(flujo_salida);
            this.buffer_writer = new BufferedWriter(stream_writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}