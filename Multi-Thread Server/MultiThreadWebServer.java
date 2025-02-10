import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class MultiThreadWebServer {
    public static void main(String[] args) {
        int puerto = 6789; // Puerto donde escuchará el servidor
        
        // Configuración del ThreadPool:
        int corePoolSize = 10;        // Número mínimo de hilos activos
        int maximumPoolSize = 50;     // Número máximo de hilos
        long keepAliveTime = 60L;     // Tiempo de inactividad para eliminar hilos extra
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100); // Cola acotada para evitar sobrecarga
        ExecutorService threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                workQueue,
                new ThreadPoolExecutor.AbortPolicy()  // Rechaza tareas si la cola está llena
        );
        
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor web multi-hilos escuchando en el puerto " + puerto);
            while (true) {
                try {
                    // Aceptar nueva conexión del cliente
                    Socket socket = serverSocket.accept();
                    System.out.println("Cliente conectado desde " + socket.getInetAddress());
                    
                    // Enviar la tarea al ThreadPool
                    threadPool.execute(new ClienteHandler(socket));
                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown(); // Liberar recursos del pool al finalizar
        }
    }
}
