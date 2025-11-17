import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Consultar implements Runnable{
    private final Thread thread;
    private final String contraseña="__n8n_BLANK_VALUE_e5362baf-c777-4d57-a609-6eaf1f9e87f6";
    private final String nombre="Pablo_Ramírez";
    public Consultar(Thread th1){
        thread = th1;
    }
    public void run(){
        try {
            File f= new File("Estadisticas.xml");
            URL url= new URL("https://n8n.z-jobs.site/webhook/calculo_Estadisticas");
            String auth_basic=nombre+":"+contraseña;
            String encode= Base64.getEncoder().encodeToString(auth_basic.getBytes(StandardCharsets.UTF_8));
            HttpsURLConnection https= (HttpsURLConnection) url.openConnection();
            https.setRequestMethod("POST");
            https.setRequestProperty("Content-Type", "application/xml");
            https.setRequestProperty("Accept", "application/xml");
            https.setRequestProperty("Authorization", auth_basic);
            https.setRequestProperty("Content-Length", String.valueOf(f.length()));
            https.setDoOutput(true);
            thread.join();
            try(FileInputStream file= new FileInputStream(f);
                DataOutputStream data= new DataOutputStream(https.getOutputStream())) {
                int leidos=0;
                byte [] buffer= new byte[1024];
                while((leidos=file.read(buffer))!=-1){
                    data.write(buffer,0,leidos);
                }
                data.flush();
            }
            try(FileOutputStream file= new FileOutputStream("Estadisticas_creadas.pdf");
                DataInputStream in= new DataInputStream(https.getInputStream());){
                in.readLine();
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
