import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadWebServer {
    public static void main(String[] args) {
        int puerto = 6666; // Puerto donde escuchará el servidor
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor web multi-hilos escuchando en el puerto " + puerto);

            while (true) {
                // Aceptar nueva conexión del cliente
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + socket.getInetAddress());
                // Crear un nuevo hilo para manejar la solicitud del cliente
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
