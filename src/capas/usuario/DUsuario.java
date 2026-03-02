package capas.usuario;

import capas.DataStore;
import capas.Database;
import capas.modelo.RolUsuario;
import java.util.Date;
import java.util.Map;

public class DUsuario {
    private Object idUsuario;
    private String nombre;
    private String email;
    private String password;
    private RolUsuario rol;
    private Date fechaRegistro;

    private final Database database;

    public DUsuario() {
        this.idUsuario = null;
        this.nombre = "";
        this.email = "";
        this.password = "";
        this.rol = RolUsuario.PARTICIPANTE;
        this.fechaRegistro = new Date();
        this.database = DataStore.getInstance().table("usuario");
    }

    public void cargarDatos(Map<String, Object> dato) {
        if (dato.containsKey("id_usuario")) this.idUsuario = dato.get("id_usuario");
        if (dato.containsKey("nombre")) this.nombre = String.valueOf(dato.get("nombre"));
        if (dato.containsKey("email")) this.email = String.valueOf(dato.get("email"));
        if (dato.containsKey("password")) this.password = String.valueOf(dato.get("password"));
        if (dato.containsKey("rol")) this.rol = RolUsuario.valueOf(String.valueOf(dato.get("rol")));
        if (dato.containsKey("fecha_registro") && dato.get("fecha_registro") instanceof Date) {
            this.fechaRegistro = (Date) dato.get("fecha_registro");
        }
    }

    public Object[] guardar() {
        Object[] data = { idUsuario, nombre, email, password, rol.name(), fechaRegistro };
        return database.store(data);
    }

    public void eliminar(String id) {
        database.delete(id);
    }

    public Object[] buscar(String id) {
        return database.find(id);
    }

    public Map<String, Object[]> listar() {
        return database.list();
    }
}

