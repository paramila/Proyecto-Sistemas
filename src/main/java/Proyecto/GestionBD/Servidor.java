import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Servidor {
    private static final int puerto = 8080;
    private static final long periodoDia= 24L * 60L *60L * 1000L;
    private static final long periodoMes= 30L * 60L * 1000L * 60L*24L;
    private static final List<Cliente> lista= new CopyOnWriteArrayList<>();
    public static void main (String [] args) {
        Timer timer = new Timer();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        Date primeraEjecucion = calendar.getTime();



        timer.schedule(new Gestion(lista), primeraEjecucion,periodoDia);
        timer.schedule(new Informacion(lista), primeraEjecucion, periodoMes);

    }
}
