import java.io.Serializable;
import jakarta.xml.bind.annotation.*;

@XmlRootElement(name="Cliente")
public class Cliente implements Serializable {

    private String nombre;
    private String telefono;
    private String codigoPostal;
    private String correo;
    private String fecha;
    public Cliente() {

    }
    public Cliente(String nombre, String telefono, String codigoPostal, String correo,String fecha) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.codigoPostal = codigoPostal;
        this.correo = correo;
        this.fecha = fecha;
    }

    @XmlElement(name="Código_postal")
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @XmlElement(name= "nombre")
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlElement(name ="telefono")
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @XmlElement(name="fecha")
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {this.fecha = fecha;}
}
