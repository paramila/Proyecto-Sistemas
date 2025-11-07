import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gestion{
    private Date hoy= new Date();
    public static  void main(String[] args){
        System.out.println("Prueba 1");
        try {
            gestion();
        }catch (IOException | SAXException e) {
            e.printStackTrace();
        }

    }
    public  static void gestion() throws IOException, SAXException {
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
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodes.item(i);
                es.execute(new Hilo(element));
            }
        }
    }


}