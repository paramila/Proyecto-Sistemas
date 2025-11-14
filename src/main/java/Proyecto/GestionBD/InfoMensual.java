import java.io.Serializable;
import java.util.List;
import jakarta.xml.bind.annotation.*;
@XmlRootElement(namespace = "es.estadisticas.sdis")
public class InfoMensual implements Serializable {
    private List<Cliente> lista;
    public InfoMensual() {
    }
    public InfoMensual(List<Cliente> lista) {
        this.lista = lista;
    }
    @XmlElementWrapper(name="listaClientes")
    @XmlElement(name="Cliente")
    public List<Cliente> getLista() {
        return lista;
    }
    public void setLista(List<Cliente> lista) {
        this.lista = lista;
    }

}
