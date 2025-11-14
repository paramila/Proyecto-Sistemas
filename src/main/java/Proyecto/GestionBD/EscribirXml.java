import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.List;

public class EscribirXml implements  Runnable{
    private List<Cliente> li;
    public EscribirXml(List<Cliente> li) {
        this.li = li;
    }
    public void run(){
        InfoMensual infoMensual = new InfoMensual();
        infoMensual.setLista(li);
        infoMensual.setNombre("Primer Semestre");

        try{
            JAXBContext jc = JAXBContext.newInstance(InfoMensual.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(infoMensual, new File("Estadisticas.xml"));

        }catch(JAXBException e){
            e.printStackTrace();
        }
    }

}
