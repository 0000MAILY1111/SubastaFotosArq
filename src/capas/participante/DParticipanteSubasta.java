package capas.participante;

import capas.DataStore;
import capas.Database;
import capas.modelo.EstadoParticipante;
import java.util.Date;
import java.util.Map;

public class DParticipanteSubasta {
    private Object idParticipanteSubasta;
    private Object idSubasta;
    private Object idUsuario;
    private Date fechaRegistro;
    private EstadoParticipante estadoParticipante;

    private final Database database;

    public DParticipanteSubasta() {
        this.idParticipanteSubasta = null;
        this.idSubasta = null;
        this.idUsuario = null;
        this.fechaRegistro = new Date();
        this.estadoParticipante = EstadoParticipante.ACTIVO;
        this.database = DataStore.getInstance().table("participante_subasta");
    }

    public void cargarDatos(Map<String, Object> dato) {
        if (dato.containsKey("id_participante_subasta")) this.idParticipanteSubasta = dato.get("id_participante_subasta");
        if (dato.containsKey("id_subasta")) this.idSubasta = dato.get("id_subasta");
        if (dato.containsKey("id_usuario")) this.idUsuario = dato.get("id_usuario");
        if (dato.containsKey("estado_participante")) {
            this.estadoParticipante = EstadoParticipante.valueOf(String.valueOf(dato.get("estado_participante")));
        }
        if (dato.containsKey("fecha_registro") && dato.get("fecha_registro") instanceof Date) {
            this.fechaRegistro = (Date) dato.get("fecha_registro");
        }
    }

    public Object[] guardar() {
        Object[] data = { idParticipanteSubasta, idSubasta, idUsuario, fechaRegistro, estadoParticipante.name() };
        return database.store(data);
    }

    public Map<String, Object[]> listar() {
        return database.list();
    }
}

