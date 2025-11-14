import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Consultar implements Runnable{
    private final Thread thread;
    public Consultar(Thread th1){
        thread = th1;
    }
    public void run(){
        try {
            URL url= new URL("https://n8n.z-jobs.site/webhook/calculo_Estadisticas");
            HttpsURLConnection https= (HttpsURLConnection) url.openConnection();
            https.setRequestMethod("POST");
            https.setRequestProperty("Content-Type", "application/xml");
            https.setRequestProperty("Accept", "application/xml");
            https.setDoOutput(true);
            thread.join();
            try(FileInputStream file= new FileInputStream("Estadisticas.xml");
                DataOutputStream data= new DataOutputStream(https.getOutputStream())) {
                int leidos=0;
                byte [] buffer= new byte[1024];
                while((leidos=file.read(buffer))!=-1){
                    data.write(buffer,0,leidos);
                }
                data.flush();
            }
            try(FileOutputStream file= new FileOutputStream("Estadisticas_creadas.pdf");
                InputStream in= https.getInputStream();){
                int leidos=0;
                byte [] buffer= new byte[1024];
                while((leidos=in.read(buffer))!=-1){
                    file.write(buffer,0,leidos);
                }
                file.flush();
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
