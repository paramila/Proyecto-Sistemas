import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gestion extends TimerTask {


    public static final String URL_GET_CITAS = "https://n8n.z-jobs.site/webhook/b00dc26f-bc0a-47e5-8df6-44af58caac5d";
    public static final String URL_SEND_EMAIL = "https://n8n.z-jobs.site/webhook/f6dfea10-df5e-4990-ac25-15792b13ecca";
    private static final int numBarberos = 3;
    private ArrayList<String> nombrebarberos= new ArrayList<>(List.of("Juan","Pablo","Esteban"));// mas alante poner una clase enumeracion con los barberos que hay
    private  final List<Cliente> lista;

    public Gestion(List<Cliente> lista) {
        this.lista = lista;
    }
    public  void run () {
        System.out.println("Iniciando Orquestador de Citas...");
        try {
            gestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void gestion() throws IOException, SAXException, ParserConfigurationException {


        System.out.println("Pidiendo citas del día a n8n (Flujo 2)...");
        String xmlRespuesta = httpGet(URL_GET_CITAS);
        System.out.println("XML Recibido de n8n:\n" + xmlRespuesta);
        if (xmlRespuesta == null || xmlRespuesta.trim().isEmpty()) {
            System.err.println("¡Error! La respuesta de n8n está vacía. No hay nada que procesar.");
            return;
        }



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xmlRespuesta)));
        Element root = document.getDocumentElement();


        NodeList nodes = root.getElementsByTagName("evento");
        System.out.println("Encontrados " + nodes.getLength() + " eventos/citas...");


        ExecutorService ex = Executors.newFixedThreadPool(numBarberos);
        for (int i = 0; i < numBarberos; i++) {
            ex.execute(new HiloBarbero(nodes,numBarberos, i,nombrebarberos.get(i)));
        }

        ex.shutdown();



        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);
                es.execute(new Hilo(element,lista));
            }
        }

        es.shutdown();
        System.out.println("Gestión completada. Esperando finalización de hilos...");
    }


    private  String httpGet(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            DataOutputStream lista= new DataOutputStream(new FileOutputStream("Fichero.xml"))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    lista.writeBytes(inputLine+"\n");
                    response.append(inputLine);
                }
                lista.flush();
                return response.toString();
            }
        } else {
            throw new IOException("GET request failed: " + responseCode);
        }
    }


    public  int httpPost(String urlString, String jsonBody) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return con.getResponseCode();
    }
}