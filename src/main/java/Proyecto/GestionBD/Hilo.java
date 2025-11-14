import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

// NOTA: Asumo que Hilo y Gestion están en el mismo paquete (directorio)
public class Hilo implements Runnable {

    private static List<Cliente> li ;
    private static final String CP_REGEX = "^\\d{5}$";
    private static final Pattern CP_PATTERN = Pattern.compile(CP_REGEX);
    private Element evento;

    public Hilo(Element element,List<Cliente> li) {
        this.evento = element;
        this.li = li;
    }

    public void run() {
        try {

            String nombre   = evento.getElementsByTagName("nombre").item(0).getTextContent().trim();
            String correo   = evento.getElementsByTagName("email").item(0).getTextContent().trim();
            String codigo_Postal  = evento.getElementsByTagName("codigo_postal").item(0).getTextContent().trim();
            String telefono = evento.getElementsByTagName("telefono").item(0).getTextContent().trim();
            String fecha    = evento.getElementsByTagName("fecha").item(0).getTextContent().trim();


            if(correo.isEmpty() ) {
                System.err.println( nombre + " no tiene email. Saltando.");
                return;
            }

            mandarCorreo(nombre, telefono, correo, fecha);
            if(!esCodigoPostalValido(codigo_Postal)) {
                codigo_Postal="";
            }

            li.add(new Cliente(nombre, telefono, codigo_Postal, correo));

        } catch (Exception e) {
            System.err.println("[Hilo " + Thread.currentThread().getId() + "] Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void mandarCorreo(String nombre, String telefono, String correo, String fecha) {
        HttpsURLConnection con = null;
        try {

            URL url = new URL(Gestion.URL_SEND_EMAIL);

            con = (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");


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


            BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea = is.readLine();
            System.out.println("[Hilo "+ Thread.currentThread().getId() + "] Respuesta  " + linea);

        } catch (IOException e) {

            System.err.println("[Hilo " + Thread.currentThread().getId() + "] Error de E/S al mandar correo: " + e.getMessage());
        } finally {
            if(con != null) {
                con.disconnect();
            }
        }
    }
    public static boolean esCodigoPostalValido(String codigoPostal) {
        if (codigoPostal == null) {
            return false;
        }

        Matcher matcher = CP_PATTERN.matcher(codigoPostal);

        return matcher.matches();
    }
}
