import java.io.*;
import java.net.*;

public class ClienteHandler implements Runnable {
    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Uso de try-with-resources para asegurar el cierre de flujos y socket
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {

            // Leer la primera línea de la solicitud HTTP
            String lineaDeSolicitud = entrada.readLine();
            if (lineaDeSolicitud == null || lineaDeSolicitud.isEmpty()) {
                socket.close();
                return;
            }
            System.out.println("Solicitud recibida: " + lineaDeSolicitud);

            // Extraer el nombre del archivo solicitado usando split()
            String[] partes = lineaDeSolicitud.split(" ");
            if (partes.length < 2 || !partes[0].equals("GET")) {
                socket.close();
                return;
            }
            String nombreArchivo = partes[1];
            if (nombreArchivo.equals("/")) {
                nombreArchivo = "/index.html"; // Página por defecto
            }
            nombreArchivo = "www" + nombreArchivo; // Directorio base "www"

            File archivo = new File(nombreArchivo);
            boolean existeArchivo = archivo.exists() && !archivo.isDirectory();

            // Construcción de la respuesta HTTP
            String lineaDeEstado;
            String lineaDeTipoContenido;
            byte[] cuerpoMensaje = null;
            if (existeArchivo) {
                lineaDeEstado = "HTTP/1.0 200 OK\r\n";
                lineaDeTipoContenido = "Content-Type: " + contentType(nombreArchivo) + "\r\n";
            } else {
                lineaDeEstado = "HTTP/1.0 404 Not Found\r\n";
                lineaDeTipoContenido = "Content-Type: text/html\r\n";
                cuerpoMensaje = "<html><body><h1>404 Not Found</h1></body></html>".getBytes();
            }

            // Enviar encabezados HTTP
            salida.writeBytes(lineaDeEstado);
            salida.writeBytes(lineaDeTipoContenido);
            salida.writeBytes("Connection: close\r\n");
            if (existeArchivo) {
                salida.writeBytes("Content-Length: " + archivo.length() + "\r\n");
            }
            salida.writeBytes("\r\n");

            // Enviar el contenido del archivo (si existe) o el mensaje de error
            if (existeArchivo) {
                try (FileInputStream fis = new FileInputStream(archivo)) {
                    enviarBytes(fis, salida);
                }
            } else {
                salida.write(cuerpoMensaje);
            }
            System.out.println("Archivo servido: " + nombreArchivo);
            socket.close();
            System.out.println("Conexión cerrada con " + socket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para enviar archivos (incluyendo archivos binarios) al cliente
    private static void enviarBytes(FileInputStream fis, OutputStream os) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesLeidos;
        while ((bytesLeidos = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesLeidos);
        }
    }

    // Método para determinar el tipo MIME en función de la extensión del archivo
    private static String contentType(String nombreArchivo) {
        if (nombreArchivo.endsWith(".html") || nombreArchivo.endsWith(".htm")) return "text/html";
        if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg")) return "image/jpeg";
        if (nombreArchivo.endsWith(".png")) return "image/png";
        if (nombreArchivo.endsWith(".gif")) return "image/gif";
        if (nombreArchivo.endsWith(".css")) return "text/css";
        if (nombreArchivo.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }
}
