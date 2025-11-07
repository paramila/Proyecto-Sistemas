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

public class Hilo implements Runnable {
    private Element cliente;

    public Hilo(Element element) {

        this.cliente = element;
    }
    public void run() {
        String nombre   = cliente.getElementsByTagName("nombre").item(0).getTextContent().trim();
        String apellido = cliente.getElementsByTagName("apellidos").item(0).getTextContent().trim();
        String correo   = cliente.getElementsByTagName("email").item(0).getTextContent().trim();
        String fecha    = cliente.getElementsByTagName("fecha").item(0).getTextContent();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse(fecha);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (sdf.format(d).equals(sdf.format(new Date()))) {
                mandarCorreo(nombre,apellido,correo,fecha);
            }



    }
    private void mandarCorreo(String nombre, String apellido, String correo, String fecha) {
        HttpsURLConnection con=null;
        try {
            URL url= new URL("https://n8n.z-jobs.site/webhook/f6dfea10-df5e-4990-ac25-15792b13ecca");
             con= (HttpsURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            StringBuilder texto = new StringBuilder();
            texto.append("{");
            texto.append("\"nombre\":\""+nombre+"\",");
            texto.append("\"apellido\":\""+apellido+"\",");
            texto.append("\"correo\":\""+correo+"\",");
            texto.append("\"fecha\":\""+fecha+"\"");
            texto.append("}");
            String enviar = texto.toString();
            try(OutputStream os = con.getOutputStream()) {
                os.write(enviar.getBytes());
                os.flush();
            }
            BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linea= is.readLine();
            System.out.println(linea);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(con!=null){
                con.disconnect();
            }
        }

    }
}
