import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

// NOTA: Asumo que Hilo y Gestion están en el mismo paquete (directorio)
public class Hilo implements Runnable {


    private Element evento;

    public Hilo(Element element) {
        this.evento = element;
    }

    public void run() {
        try {
            // --- 1. LEER LOS DATOS DEL XML (Corregido) ---
            String nombre   = evento.getElementsByTagName("nombre").item(0).getTextContent().trim();
            String correo   = evento.getElementsByTagName("email").item(0).getTextContent().trim();

            String telefono = evento.getElementsByTagName("telefono").item(0).getTextContent().trim();
            String fecha    = evento.getElementsByTagName("fecha").item(0).getTextContent().trim();

            if(correo.isEmpty()) {
                System.err.println("[Hilo " + Thread.currentThread().getId() + "] " + nombre + " no tiene email. Saltando.");
                return;
            }
            mandarCorreo(nombre, telefono, correo, fecha);


        } catch (Exception e) {
            System.err.println("[Hilo " + Thread.currentThread().getId() + "] Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- 3. MANDAR CORREO (Corregido) ---
    private void mandarCorreo(String nombre, String telefono, String correo, String fecha) {
        HttpsURLConnection con = null;
        try {
            // ¡¡CAMBIO!! Usamos la variable estática de Gestion
            URL url = new URL(Gestion.URL_SEND_EMAIL);

            con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            // ¡¡CAMBIO!! Enviamos 'telefono', no 'apellido'
            StringBuilder texto = new StringBuilder();
            texto.append("{");
            texto.append("\"nombre\":\"").append(nombre).append("\",");
            texto.append("\"telefono\":\"").append(telefono).append("\",");
            texto.append("\"correo\":\"").append(correo).append("\",");
            texto.append("\"fecha\":\"").append(fecha).append("\"");
            texto.append("}");

            String enviar = texto.toString();

            try(OutputStream os = con.getOutputStream()) {
                os.write(enviar.getBytes());
                os.flush();
            }

            // Leemos la respuesta de n8n
            BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea = is.readLine();
            System.out.println("[Hilo "+ Thread.currentThread().getId() + "] Respuesta de n8n: " + linea);

        } catch (IOException e) {
            // Lo cambiamos para que no pare la ejecución de otros hilos
            System.err.println("[Hilo " + Thread.currentThread().getId() + "] Error de E/S al mandar correo: " + e.getMessage());
        } finally {
            if(con != null) {
                con.disconnect();
            }
        }
    }
}
