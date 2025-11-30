import java.io.Serializable;
import java.util.List;
import jakarta.xml.bind.annotation.*;
@XmlRootElement(namespace = "infomensual")
public class InfoMensual implements Serializable {
    private List<Cliente> lista;
    private String nombre;
    public InfoMensual() {
    }

    @XmlElementWrapper(name="listaClientes")
    @XmlElement(name="Cliente")
    public List<Cliente> getLista() {
        return lista;
    }
    public void setLista(List<Cliente> lista) {
        this.lista = lista;
    }
    @XmlElement(name="fecha")
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
