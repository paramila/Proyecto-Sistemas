import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.List;
import java.util.TimerTask;

public class Informacion  extends TimerTask {
    private List<Cliente> clientes;
    public Informacion(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    public void run() {
        Thread th1= new Thread(new EscribirXml(clientes));
        Thread th2= new Thread(new Consultar(th1));
        th1.start();


        analizarEstadisticas("Estadisticas.xml");
    }
    private void analizarEstadisticas(String fileName) {

    }
}
