import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
/**
 * Clase que implementa la lógica de un "Barbero" como una tarea ejecutable (Runnable).
 *
 * Esta clase está diseñada para ser ejecutada por un hilo dentro de un pool fijo.
 * Su propósito es implementar una asignación de tipo Round-Robin (reparto equitativo)
 * de una lista de clientes (Nodos XML) obtenida de Gestion.java.
 *
 * Cada hilo recibe la lista completa de nodos (clientes) y un ID único de barbero.
 * Utiliza este ID para procesar únicamente los clientes que le corresponden
 * (ej. Barbero 0 procesa 0, 3, 6...; Barbero 1 procesa 1, 4, 7...).
 *
 * El hilo acumula la información de los clientes asignados en un StringBuilder
 * y, al finalizar su bucle, escribe el contenido completo en un archivo de log
 * local (ej. "Clientes_de_Barbero_0.txt").
 *
 * Cuando me de tiempo a comporbar que se crean bien los archivo me encargare de :
 *     1_ Añadir el correo de los barberos o de la barberia y
 *     2_ enviar cada archivo(cosa que no sera muy dificil)
 */
public class HiloBarbero implements Runnable {

    private int idBarbero;
    private int numBarberos;
    private NodeList nodes ;
    private String nombreBarbero;




    public HiloBarbero(NodeList nodes, int numBarberos,int idBarbero,String nombreBarbero) {
        this.idBarbero = idBarbero;
        this.numBarberos = numBarberos;
        this.nodes = nodes;
        this.nombreBarbero = nombreBarbero;

    }

    @Override
    public void run() {
        int contador = idBarbero;
        String nombreArchivo = "Clientes_de_" + this.nombreBarbero + ".txt";
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("--- Log de Clientes Asignados al Barbero ").append(nombreBarbero).append(" ---\n");
        Element e;
        while (contador < nodes.getLength()) {
            e = (Element) nodes.item(contador);
            String nombre = e.getElementsByTagName("nombre").item(0).getTextContent().trim();
            String correo = e.getElementsByTagName("email").item(0).getTextContent().trim();
            String telefono = e.getElementsByTagName("telefono").item(0).getTextContent().trim();
            String fecha = e.getElementsByTagName("fecha").item(0).getTextContent().trim();
            String fecha_buena= fecha.replace('T',' ');
            String fecha_final=fecha_buena.substring(0,19);
            String lneaCliente= "El cliente :"+nombre+" tiene cita hoy a fecha y hora : "+fecha_final+". \n";
            logBuilder.append(lneaCliente);

            contador=contador+numBarberos;
        }
        // con esto escribo el string completo en el archivo local
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.print(logBuilder.toString());
        } catch (IOException ioEx) {
            System.err.println("[Barbero " + idBarbero + "] Error CRÍTICO al escribir el archivo de log: " + ioEx.getMessage());
        }


    }

}