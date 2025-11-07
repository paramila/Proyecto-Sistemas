import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gestion{
    private Date hoy= new Date();
    public void gestion() throws IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = builder.parse(new File("base.xml"));
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);
                String nombre = element.getAttribute("nombre");
                String apellido = element.getAttribute("apellido");
                String correo = element.getAttribute("correo");
                String fecha = element.getAttribute("fecha");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d = sdf.parse(fecha);
                    Date hoy2= sdf.parse(String.valueOf(hoy));
                    if(d.equals(hoy2)) {
                        mandarCorreo(nombre,apellido,correo,fecha);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }




            }
        }
    }

}